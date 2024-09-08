package space.kscience.visionforge.html

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import space.kscience.dataforge.context.Global
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.set
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionGroup
import space.kscience.visionforge.VisionManager
import space.kscience.visionforge.visionManager
import kotlin.collections.set
import kotlin.test.Test

typealias HtmlVisionRenderer = FlowContent.(name: Name, vision: Vision, meta: Meta) -> Unit

fun FlowContent.renderVisionFragment(
    renderer: DIV.(name: Name, vision: Vision, meta: Meta) -> Unit,
    idPrefix: String? = null,
    fragment: HtmlVisionFragment,
): Map<Name, Vision> {
    val visionMap = HashMap<Name, Vision>()
    val consumer = object : VisionTagConsumer<Any?>(consumer, Global.visionManager, idPrefix) {
        override fun DIV.renderVision(manager: VisionManager, name: Name, vision: Vision, outputMeta: Meta) {
            visionMap[name] = vision
            renderer(name, vision, outputMeta)
        }
    }
    fragment.appendTo(consumer)
    return visionMap
}

class HtmlTagTest {

    val fragment = HtmlVisionFragment {
        div {
            h1 { +"Head" }
            vision("ddd") {
                meta {
                    "metaProperty" put 87
                }
                VisionGroup {
                    properties["myProp"] = 82
                    properties["otherProp"] = false
                }
            }
        }
    }

    val simpleVisionRenderer: HtmlVisionRenderer = { _, vision, _ ->
        div {
            h2 { +"Properties" }
            ul {
                vision.properties.items.forEach {
                    li {
                        a { +it.key.toString() }
                        p { +it.value.toString() }
                    }
                }
            }
        }
    }


    @Test
    fun testStringRender() {
        println(
            createHTML().div {
                renderVisionFragment(simpleVisionRenderer, fragment = fragment)
            }
        )
    }
}