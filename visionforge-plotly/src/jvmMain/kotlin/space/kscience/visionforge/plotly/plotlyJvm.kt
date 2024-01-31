package space.kscience.visionforge.plotly

import kotlinx.serialization.modules.SerializersModule
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.PluginFactory
import space.kscience.dataforge.context.PluginTag
import space.kscience.dataforge.meta.Meta
import space.kscience.visionforge.VisionPlugin

public actual class PlotlyPlugin : VisionPlugin() {

    actual override val tag: PluginTag get() = Companion.tag

    actual override val visionSerializersModule: SerializersModule get() = plotlySerializersModule

    public actual companion object : PluginFactory<PlotlyPlugin> {
        actual override val tag: PluginTag = PluginTag("vision.plotly", PluginTag.DATAFORGE_GROUP)

        actual override fun build(context: Context, meta: Meta): PlotlyPlugin = PlotlyPlugin()

    }
}