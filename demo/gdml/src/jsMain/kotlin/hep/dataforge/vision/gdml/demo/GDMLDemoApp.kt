package hep.dataforge.vision.gdml.demo

import hep.dataforge.context.Global
import hep.dataforge.js.Application
import hep.dataforge.js.startApplication
import hep.dataforge.vision.gdml.GDMLTransformer
import hep.dataforge.vision.gdml.LUnit
import hep.dataforge.vision.gdml.toVision
import hep.dataforge.vision.solid.SolidMaterial.Companion.MATERIAL_OPACITY_KEY
import kotlinx.css.*
import react.child
import react.dom.render
import styled.injectGlobal
import kotlin.browser.document


val gdmlConfiguration: GDMLTransformer.() -> Unit = {
    lUnit = LUnit.CM
    volumeAction = { volume ->
        when {
            volume.name.startsWith("ecal01lay") -> GDMLTransformer.Action.REJECT
            volume.name.startsWith("UPBL") -> GDMLTransformer.Action.REJECT
            volume.name.startsWith("USCL") -> GDMLTransformer.Action.REJECT
            volume.name.startsWith("VPBL") -> GDMLTransformer.Action.REJECT
            volume.name.startsWith("VSCL") -> GDMLTransformer.Action.REJECT
            else -> GDMLTransformer.Action.CACHE
        }
    }

    solidConfiguration = { parent, solid ->
        if (
            solid.name.startsWith("Yoke")
            || solid.name.startsWith("Pole")
            || parent.physVolumes.isNotEmpty()
        ) {
            useStyle("opaque") {
                MATERIAL_OPACITY_KEY put 0.3
            }
        }
    }
}

private class GDMLDemoApp : Application {

    override fun start(state: Map<String, Any>) {

        injectGlobal {
            body {
                height = 100.pct
                width = 100.pct
                margin(0.px)
                padding(0.px)
            }
        }

        val context = Global.context("demo") {}
        val element = document.getElementById("app") ?: error("Element with id 'app' not found on page")

        render(element) {
            child(GDMLApp) {
                attrs {
                    this.context = context
                    this.rootObject = cubes().toVision(gdmlConfiguration)
                }
            }
        }
//        (document.getElementById("file_load_button") as? HTMLInputElement)?.apply {
//            addEventListener("change", {
//                (it.target as HTMLInputElement).files?.asList()?.first()?.let { file ->
//                    FileReader().apply {
//                        onload = {
//                            val string = result as String
//                            action(file.name, string)
//                        }
//                        readAsText(file)
//                    }
//                }
//            }, false)
//        }

    }
}

fun main() {
    startApplication(::GDMLDemoApp)
}