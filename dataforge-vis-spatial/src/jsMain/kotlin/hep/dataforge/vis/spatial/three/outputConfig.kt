package hep.dataforge.vis.spatial.three

import hep.dataforge.vis.js.editor.card
import kotlinx.html.InputType
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.input
import kotlinx.html.js.label
import org.w3c.dom.Element
import kotlin.dom.clear

fun Element.threeOutputConfig(canvas: ThreeCanvas) {
    clear()
    append {
        card("Layers"){
            div("row") {
                (0..11).forEach { layer ->
                    div("col-1") {
                        label { +layer.toString() }
                        input(type = InputType.checkBox).apply {
                            if (layer == 0) {
                                checked = true
                            }
                            onchange = {
                                if (checked) {
                                    canvas.camera.layers.enable(layer)
                                } else {
                                    canvas.camera.layers.disable(layer)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}