package space.kscience.visionforge.plotly

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.PluginFactory
import space.kscience.dataforge.context.PluginTag
import space.kscience.dataforge.meta.Meta
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionPlugin

public expect class PlotlyPlugin : VisionPlugin {

    override val tag: PluginTag
    override val visionSerializersModule: SerializersModule

    public companion object : PluginFactory<PlotlyPlugin>{
        override fun build(context: Context, meta: Meta): PlotlyPlugin

        override val tag: PluginTag
    }
}

internal val plotlySerializersModule = SerializersModule {
    polymorphic(Vision::class) {
        subclass(VisionOfPlotly.serializer())
    }
}