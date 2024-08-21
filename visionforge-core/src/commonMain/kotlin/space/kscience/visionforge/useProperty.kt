package space.kscience.visionforge

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.descriptors.get
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.parseAsName
import space.kscience.dataforge.names.startsWith
import kotlin.reflect.KProperty1


private fun Vision.inheritedEventFlow(): Flow<VisionEvent> =
    parent?.let { parent -> merge(eventFlow, parent.inheritedEventFlow()) } ?: eventFlow

/**
 * Call [callback] on initial value of the property and then on all subsequent values after change
 */
public fun Vision.useProperty(
    propertyName: Name,
    inherited: Boolean = descriptor?.get(propertyName)?.inherited ?: false,
    useStyles: Boolean = descriptor?.get(propertyName)?.usesStyles ?: true,
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend (Meta) -> Unit,
): Job = scope.launch {
    //Pass initial value synchronously

    callback(getProperty(propertyName, inherited, useStyles))

    val combinedFlow = if (inherited) {
        inheritedEventFlow()
    } else {
        eventFlow
    }

    combinedFlow.filterIsInstance<VisionPropertyChangedEvent>().onEach {
        if (it.property == propertyName || (useStyles && it.property == Vision.STYLE_KEY)) {
            callback(getProperty(propertyName, inherited, useStyles))
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
): Job = useProperty(property.name, scope = scope){
    callback(property.get(this))
}

/**
 * Subscribe on property updates. The subscription is bound to the given scope and canceled when the scope is canceled
 */
public fun Vision.onPropertyChange(
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend (Name) -> Unit,
): Job = properties.changes.onEach {
    callback(it)
}.launchIn(scope)

/**
 * Observe changes to the specific property without passing the initial value.
 */
public fun <V : Vision, T> V.onPropertyChange(
    property: KProperty1<V, T>,
    scope: CoroutineScope = manager?.context ?: error("Orphan Vision can't observe properties. Use explicit scope."),
    callback: suspend V.(T) -> Unit,
): Job = properties.changes.filter { it.startsWith(property.name.asName()) }.onEach {
    callback(property.get(this))
}.launchIn(scope)