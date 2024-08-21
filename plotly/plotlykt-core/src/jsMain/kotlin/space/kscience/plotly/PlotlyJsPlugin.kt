package space.kscience.plotly

import kotlinx.serialization.modules.SerializersModule
import org.w3c.dom.Element
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.PluginFactory
import space.kscience.dataforge.context.PluginTag
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionPlugin
import space.kscience.visionforge.html.ElementVisionRenderer
import space.kscience.visionforge.html.JsVisionClient

public class PlotlyJSPlugin : VisionPlugin(), ElementVisionRenderer {
    public val plotly: PlotlyPlugin by require(PlotlyPlugin)
    public val visionClient: JsVisionClient by require(JsVisionClient)

    override val tag: PluginTag get() = Companion.tag

    override val visionSerializersModule: SerializersModule get() = plotlySerializersModule

    override fun rateVision(vision: Vision): Int = when (vision) {
        is Plot -> ElementVisionRenderer.DEFAULT_RATING
        else -> ElementVisionRenderer.ZERO_RATING
    }

    override fun render(element: Element, name: Name, vision: Vision, meta: Meta) {
        val plot = (vision as? Plot) ?: error("VisionOfPlotly expected but ${vision::class} found")
        val config = PlotlyConfig.read(meta)
        element.plot(config, plot)
    }

    override fun toString(): String = "Plotly"

    override fun content(target: String): Map<Name, Any> = when (target) {
        ElementVisionRenderer.TYPE -> mapOf("plotly".asName() to this)
        else -> super.content(target)
    }

    public  companion object : PluginFactory<PlotlyJSPlugin> {
        override val tag: PluginTag = PluginTag("vision.plotly.js", PluginTag.DATAFORGE_GROUP)

        override fun build(context: Context, meta: Meta): PlotlyJSPlugin = PlotlyJSPlugin()

    }
}