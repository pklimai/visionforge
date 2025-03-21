import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.openInBrowser
import space.kscience.plotly.trace
import space.kscience.visionforge.html.ResourceLocation
import kotlin.math.PI
import kotlin.math.sin


fun main() {
    val x1 = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x1.map { sin(2.0 * PI * it) }

    val plot = Plotly.plot {
        trace {
            x.set(x1)
            y.set(y1)
            name = "for a single trace in graph its name would be hidden"
        }
        layout {
            title = "Graph name"
            xaxis {
                title = "x axis"
            }
            yaxis {
                title = "y axis"
            }
        }
    }

    plot.openInBrowser(resourceLocation = ResourceLocation.EMBED)
}