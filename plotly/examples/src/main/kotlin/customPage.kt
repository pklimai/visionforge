import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.hr
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import space.kscience.plotly.page
import space.kscience.plotly.staticPlot
import space.kscience.visionforge.html.openInBrowser
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {

    val x1 = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x1.map { sin(2.0 * PI * it) }
    val y2 = x1.map { cos(2.0 * PI * it) }

    val trace1 = Trace(x1, y1) { name = "sin" }
    val trace2 = Trace(x1, y2) { name = "cos" }

    Plotly.page {
        staticPlot{
            traces(trace1, trace2)
            layout {
                title = "The plot above"
                xaxis.title = "x axis name"
                yaxis.title = "y axis name"
            }
        }
        hr()
        h1 { +"A custom separator" }
        hr()
        div {
            staticPlot {
                traces(trace1, trace2)
                layout {
                    title = "The plot below"
                    xaxis.title = "x axis name"
                    yaxis.title = "y axis name"
                }
            }
        }
    }.openInBrowser()
}
