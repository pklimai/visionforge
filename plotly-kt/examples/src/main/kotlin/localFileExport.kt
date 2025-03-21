import space.kscience.plotly.*
import kotlin.math.PI
import kotlin.math.sin


@UnstablePlotlyAPI
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

    plot.makeFile(Plotly.selectFile()?:error("File not selected"))
    //plot.makeFile(Files.createTempFile("plotlykt",".html"))
}