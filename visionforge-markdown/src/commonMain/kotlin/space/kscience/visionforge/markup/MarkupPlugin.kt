package space.kscience.visionforge.markup

import kotlinx.serialization.modules.SerializersModule
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.PluginFactory
import space.kscience.dataforge.context.PluginTag
import space.kscience.dataforge.meta.Meta
import space.kscience.visionforge.VisionPlugin

public class MarkupPlugin : VisionPlugin() {
    override val visionSerializersModule: SerializersModule get() = markupSerializersModule

    override val tag: PluginTag get() = Companion.tag

    public companion object : PluginFactory<MarkupPlugin> {
        override val tag: PluginTag = PluginTag("vision.markup", PluginTag.DATAFORGE_GROUP)

        override fun build(context: Context, meta: Meta): MarkupPlugin = MarkupPlugin()

    }
}