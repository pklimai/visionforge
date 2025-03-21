import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable
import space.kscience.dataforge.context.Context
import space.kscience.plotly.PlotlyJsPlugin
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.scatter
import space.kscience.visionforge.Colors
import space.kscience.visionforge.html.Tabs
import space.kscience.visionforge.html.VisionForgeStyles
import space.kscience.visionforge.html.startApplication
import space.kscience.visionforge.markup.MarkupJsPlugin
import space.kscience.visionforge.solid.*
import space.kscience.visionforge.solid.three.ThreePlugin
import space.kscience.visionforge.solid.three.compose.ThreeView
import kotlin.random.Random

fun Trace.appendXYLatest(x: Number, y: Number, history: Int = 400, xErr: Number? = null, yErr: Number? = null) {
    this.x.numbers = (this.x.numbers + x).takeLast(history)
    this.y.numbers = (this.y.numbers + y).takeLast(history)
    xErr?.let { error_x.array = (error_x.array + xErr).takeLast(history) }
    yErr?.let { error_y.array = (error_y.array + yErr).takeLast(history) }
}

public fun main() {
    val playgroundContext = Context {
        plugin(ThreePlugin)
        plugin(PlotlyJsPlugin)
        plugin(MarkupJsPlugin)
    }
    startApplication { document ->

//        val solids = playgroundContext.request(Solids)
//        val client = playgroundContext.request(JsVisionClient)

        val element = document.getElementById("playground") ?: error("Element with id 'playground' not found on page")

        renderComposable(element) {
            Style(VisionForgeStyles)
            Div({
                style {
                    padding(0.pt)
                    margin(0.pt)
                    height(100.vh)
                    width(100.vw)
                }
            }) {
                Tabs("gravity") {
                    Tab("gravity") {
                        GravityDemo(playgroundContext)
                    }

//                    Tab("D0") {
//                        child(ThreeCanvasWithControls) {
//                            attrs {
//                                context = playgroundContext
//                                solid = GdmlShowCase.babyIaxo().toVision()
//                            }
//                        }
//                    }
                    Tab("spheres") {
                        Div({
                            style {
                                height(100.vh - 50.pt)
                            }
                        }) {
                            ThreeView(playgroundContext, SolidGroup {
                                ambientLight {
                                    color(Colors.white)
                                }
                                repeat(100) {
                                    sphere(5, name = "sphere[$it]") {
                                        x = Random.nextDouble(-300.0, 300.0)
                                        y = Random.nextDouble(-300.0, 300.0)
                                        z = Random.nextDouble(-300.0, 300.0)
                                        material {
                                            color(Random.nextInt())
                                        }
                                        detail = 16
                                    }
                                }
                            })
                        }
                    }
                    Tab("plotly") {
                        Plot(playgroundContext) {
                            scatter {
                                x(1, 2, 3)
                                y(5, 8, 7)
                            }
                        }
                    }
                }
            }
        }
    }
}