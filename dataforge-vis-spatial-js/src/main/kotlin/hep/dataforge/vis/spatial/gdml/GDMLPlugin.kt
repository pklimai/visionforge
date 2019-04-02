package hep.dataforge.vis.spatial.gdml

import hep.dataforge.context.AbstractPlugin
import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.vis.spatial.ThreePlugin

class GDMLPlugin : AbstractPlugin() {
    override val tag: PluginTag get() = GDMLPlugin.tag

    override fun dependsOn() = listOf(ThreePlugin)

    override fun attach(context: Context) {
        super.attach(context)
//        context.plugins.get<ThreePlugin>()?.factories?.apply {
//            this["jsRoot.geometry".toName()] = ThreeJSRootGeometryFactory
//            this["jsRoot.object".toName()] = ThreeJSRootObjectFactory
//        }
    }

    override fun detach() {
//        context.plugins.get<ThreePlugin>()?.factories?.apply {
//            remove("jsRoot.geometry".toName())
//            remove("jsRoot.object".toName())
//        }
        super.detach()
    }

    companion object : PluginFactory<GDMLPlugin> {
        override val tag = PluginTag("vis.gdml", "hep.dataforge")
        override val type = GDMLPlugin::class
        override fun invoke() = GDMLPlugin()
    }
}