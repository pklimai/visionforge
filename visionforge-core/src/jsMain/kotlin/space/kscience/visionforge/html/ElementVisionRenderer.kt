package space.kscience.visionforge.html

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.misc.DfType
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.Vision
import kotlin.reflect.cast

/**
 * A browser renderer for a [Vision].
 */
@DfType(ElementVisionRenderer.TYPE)
public interface ElementVisionRenderer {

    /**
     * Give a [vision] integer rating based on this renderer capabilities. [ZERO_RATING] or negative values means that this renderer
     * can't process a vision. The value of [DEFAULT_RATING] used for default renderer. Specialized renderers could specify
     * higher value to "steal" rendering job
     */
    public fun rateVision(vision: Vision): Int

    /**
     * Display the [vision] inside a given [element] replacing its current content.
     * @param meta additional parameters for rendering container
     */
    public fun render(
        element: Element,
        name: Name,
        vision: Vision,
        meta: Meta = Meta.EMPTY,
    )

    override fun toString(): String

    public companion object {
        public const val TYPE: String = "elementVisionRenderer"
        public const val ZERO_RATING: Int = 0
        public const val DEFAULT_RATING: Int = 10
    }
}

/**
 * A browser renderer for a vision of given type
 */
public inline fun <reified T : Vision> ElementVisionRenderer(
    acceptRating: Int = ElementVisionRenderer.DEFAULT_RATING,
    noinline renderFunction: TagConsumer<HTMLElement>.(name: Name, vision: T, meta: Meta) -> Unit,
): ElementVisionRenderer = object : ElementVisionRenderer {

    override fun rateVision(vision: Vision): Int =
        if (vision::class == T::class) acceptRating else ElementVisionRenderer.ZERO_RATING

    override fun render(
        element: Element,
        name: Name,
        vision: Vision,
        meta: Meta,
    ) {
        element.clear()
        element.append {
            renderFunction(name, T::class.cast(vision), meta)
        }
    }

    override fun toString(): String = "ElementVisionRender(${T::class.simpleName})"
}
