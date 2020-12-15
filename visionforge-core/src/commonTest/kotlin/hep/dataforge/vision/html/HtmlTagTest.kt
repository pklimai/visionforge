package hep.dataforge.vision.html

import hep.dataforge.meta.DFExperimental
import hep.dataforge.meta.set
import hep.dataforge.vision.VisionBase
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlin.test.Test

class HtmlTagTest {

    @OptIn(DFExperimental::class)
    fun VisionOutput.base(block: VisionBase.() -> Unit) =
        VisionBase().apply(block)

    val fragment: HtmlVisionFragment = {
        div {
            h1 { +"Head" }
            vision("ddd") {
                meta {
                    "metaProperty" put 87
                }
                base {
                    configure {
                        set("myProp", 82)
                        set("otherProp", false)
                    }
                }
            }
        }
    }

    val simpleVisionRenderer: HtmlVisionRenderer = { _, vision, _ ->
        div {
            h2 { +"Properties" }
            ul {
                (vision as? VisionBase)?.ownProperties?.items?.forEach {
                    li {
                        a { +it.key.toString() }
                        p { +it.value.toString() }
                    }
                }
            }
        }
    }

    val groupRenderer: HtmlVisionRenderer = { _, group, _ ->
        p { +"This is group" }
    }


    @Test
    fun testStringRender() {
        println(
            createHTML().div {
                renderVisionFragment<String>(simpleVisionRenderer, fragment = fragment)
            }
        )
    }
}