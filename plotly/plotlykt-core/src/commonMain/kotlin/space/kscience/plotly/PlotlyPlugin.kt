package space.kscience.plotly

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.PluginFactory
import space.kscience.dataforge.context.PluginTag
import space.kscience.dataforge.meta.Meta
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionPlugin

public class PlotlyPlugin : VisionPlugin() {

    override val tag: PluginTag get() = Companion.tag

    override val visionSerializersModule: SerializersModule get() = plotlySerializersModule

    public companion object : PluginFactory<PlotlyPlugin> {
        override val tag: PluginTag = PluginTag("vision.plotly", PluginTag.DATAFORGE_GROUP)

        override fun build(context: Context, meta: Meta): PlotlyPlugin = PlotlyPlugin()

    }
}

internal val plotlySerializersModule = SerializersModule {
    polymorphic(Vision::class) {
        subclass(Plot.serializer())
    }
}