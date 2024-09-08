package space.kscience.visionforge

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.descriptors.get
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.parseAsName
import kotlin.reflect.KProperty1

private fun Vision.inheritedEventFlow(): Flow<VisionEvent> =
    parent?.let { parent -> merge(eventFlow, parent.inheritedEventFlow()) } ?: eventFlow

/**
 * Create a flow of a specific property
 */
public fun Vision.flowProperty(
    propertyName: Name,
    inherited: Boolean = isInheritedProperty(propertyName),
    useStyles: Boolean = isStyledProperty(propertyName),
): Flow<Meta> = flow {
    //Pass initial value.
    readProperty(propertyName, inherited, useStyles)?.let { emit(it) }

    val combinedFlow: Flow<VisionEvent> = if (inherited) {
        inheritedEventFlow()
    } else {
        eventFlow
    }

    combinedFlow.filterIsInstance<VisionPropertyChangedEvent>().collect { event ->
        if (event.propertyName == propertyName || (useStyles && event.propertyName == Vision.STYLE_KEY)) {
            readProperty(event.propertyName, inherited, useStyles)?.let { emit(it) }
        }
    }
}

public fun Vision.flowProperty(
    propertyName: String,
    inherited: Boolean = isInheritedProperty(propertyName),
    useStyles: Boolean = isStyledProperty(propertyName),
): Flow<Meta> = flowProperty(propertyName.parseAsName(), inherited, useStyles)

/**
 * Flow the value of specific property
 */
public fun Vision.flowPropertyValue(
    propertyName: Name,
    inherited: Boolean = isInheritedProperty(propertyName),
    useStyles: Boolean = isStyledProperty(propertyName),
): Flow<Value?> = flowProperty(propertyName, inherited, useStyles).map { it.value }

public fun Vision.flowPropertyValue(
    propertyName: String,
    inherited: Boolean = isInheritedProperty(propertyName),
    useStyles: Boolean = isStyledProperty(propertyName),
): Flow<Value?> = flowPropertyValue(propertyName.parseAsName(), inherited, useStyles)

///

/**
 * Call [callback] on initial value of the property and then on all subsequent values after change
 */
public fun Vision.useProperty(
    propertyName: Name,
    inherited: Boolean = isInheritedProperty(propertyName),
    useStyles: Boolean = isStyledProperty(propertyName),
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend (Meta) -> Unit,
): Job = scope.launch {
    //Pass initial value synchronously

    readProperty(propertyName, inherited, useStyles)?.let { callback(it) }

    val combinedFlow = if (inherited) {
        inheritedEventFlow()
    } else {
        eventFlow
    }

    combinedFlow.filterIsInstance<VisionPropertyChangedEvent>().onEach { event ->
        if (event.propertyName == propertyName || (useStyles && event.propertyName == Vision.STYLE_KEY)) {
            readProperty(event.propertyName, inherited, useStyles)?.let { callback(it) }
        }
    }.collect()
}

public fun Vision.useProperty(
    propertyName: String,
    inherited: Boolean = descriptor?.get(propertyName)?.inherited ?: false,
    useStyles: Boolean = descriptor?.get(propertyName)?.usesStyles ?: true,
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend (Meta) -> Unit,
): Job = useProperty(propertyName.parseAsName(), inherited, useStyles, scope, callback)

public fun <V : Vision, T> V.useProperty(
    property: KProperty1<V, T>,
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend V.(T) -> Unit,
): Job = useProperty(property.name, scope = scope) {
    callback(property.get(this))
}

/**
 * Subscribe on property updates. The subscription is bound to the given scope and canceled when the scope is canceled
 */
public fun Vision.onPropertyChange(
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend (Name) -> Unit,
): Job = inheritedEventFlow().filterIsInstance<VisionPropertyChangedEvent>().onEach {
    callback(it.propertyName)
}.launchIn(scope)

/**
 * Observe changes to the specific property without passing the initial value.
 */
public fun <V : Vision, T> V.onPropertyChange(
    property: KProperty1<V, T>,
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend V.(T) -> Unit,
): Job = inheritedEventFlow().filterIsInstance<VisionPropertyChangedEvent>().onEach {
    if (it.propertyName.toString() == property.name) {
        callback(property.get(this))
    }
}.launchIn(scope)