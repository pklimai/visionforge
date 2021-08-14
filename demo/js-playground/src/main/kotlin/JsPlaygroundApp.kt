import kotlinx.browser.document
import kotlinx.css.*
import react.child
import react.dom.render
import ringui.SmartTabs
import ringui.Tab
import space.kscience.dataforge.context.Context
import space.kscience.gdml.GdmlShowCase
import space.kscience.plotly.scatter
import space.kscience.visionforge.Application
import space.kscience.visionforge.VisionClient
import space.kscience.visionforge.gdml.toVision
import space.kscience.visionforge.plotly.PlotlyPlugin
import space.kscience.visionforge.ring.ThreeCanvasWithControls
import space.kscience.visionforge.ring.ThreeWithControlsPlugin
import space.kscience.visionforge.solid.*
import space.kscience.visionforge.startApplication
import styled.css
import styled.styledDiv
import kotlin.random.Random

private class JsPlaygroundApp : Application {

    override fun start(state: Map<String, Any>) {

        val playgroundContext = Context {
            plugin(ThreeWithControlsPlugin)
            plugin(VisionClient)
            plugin(PlotlyPlugin)
        }

        val element = document.getElementById("playground") ?: error("Element with id 'playground' not found on page")

        val visionOfD0 = GdmlShowCase.babyIaxo().toVision()

        val random = Random(112233)
        val visionOfSpheres = SolidGroup {
            repeat(100) {
                sphere(5, name = "sphere[$it]") {
                    x = random.nextDouble(-300.0, 300.0)
                    y = random.nextDouble(-300.0, 300.0)
                    z = random.nextDouble(-300.0, 300.0)
                    material {
                        color(random.nextInt())
                    }
                    detail = 16
                }
            }
        }

        render(element) {
            styledDiv {
                css {
                    padding(0.pt)
                    margin(0.pt)
                    height = 100.vh
                    width = 100.vw
                }
                SmartTabs("D0") {
                    Tab("D0") {
                        child(ThreeCanvasWithControls) {
                            attrs {
                                context = playgroundContext
                                solid = visionOfD0
                            }
                        }
                    }
                    Tab("spheres") {
                        child(ThreeCanvasWithControls) {
                            attrs {
                                context = playgroundContext
                                solid = visionOfSpheres
                            }
                        }
                    }
                    Tab("plotly"){
                        Plotly{
                            attrs {
                                context = playgroundContext
                                plot = space.kscience.plotly.Plotly.plot {
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
    }
}

public fun main() {
    startApplication(::JsPlaygroundApp)
}