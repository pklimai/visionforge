import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.trace
import kotlin.math.PI
import kotlin.math.sin

fun main() {
    val xValues = (0..100).map { it.toDouble() / 100.0 }
    val yValues = xValues.map { sin(2.0 * PI * it) }

    val plot = Plotly.plot {
        trace {
            x.set(xValues)
            y.set(yValues)
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

    plot.makeFile()
}
