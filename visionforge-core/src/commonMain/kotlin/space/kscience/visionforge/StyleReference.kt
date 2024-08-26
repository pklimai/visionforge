package space.kscience.visionforge

import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.Scheme
import space.kscience.dataforge.meta.SchemeSpec
import space.kscience.dataforge.names.parseAsName
import kotlin.properties.ReadOnlyProperty

/**
 * A reference to a style defined in a specific container
 */
public class StyleReference(public val owner: Vision, public val name: String)

private tailrec fun styleIsDefined(vision: Vision, reference: StyleReference): Boolean = when {
    reference.owner === vision -> true
    vision.parent == null -> false
    else -> styleIsDefined(vision.parent!!, reference)
}

@VisionBuilder
public fun Vision.useStyle(reference: StyleReference, notify: Boolean = true) {
    //check that style is defined in a parent
    //check(styleIsDefined(this, reference)) { "Style reference does not belong to a Vision parent" }
    useStyle(reference.name, notify)
}

@VisionBuilder
public fun MutableVision.style(
    styleKey: String? = null,
    builder: MutableMeta.() -> Unit,
): ReadOnlyProperty<Any?, StyleReference> = ReadOnlyProperty { _, property ->
    val styleName = styleKey ?: property.name
    updateStyle(styleName.parseAsName(), builder)
    StyleReference(this, styleName)
}

@VisionBuilder
public fun <T : Scheme> MutableVision.style(
    spec: SchemeSpec<T>,
    styleKey: String? = null,
    builder: T.() -> Unit,
): ReadOnlyProperty<Any?, StyleReference> = ReadOnlyProperty { _, property ->
    val styleName = styleKey ?: property.name
    updateStyle(styleName.parseAsName(),  spec(builder))
    StyleReference(this, styleName)
}