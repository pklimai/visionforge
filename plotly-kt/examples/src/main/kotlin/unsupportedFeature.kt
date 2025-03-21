import space.kscience.dataforge.meta.*
import space.kscience.plotly.Plotly
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import space.kscience.plotly.openInBrowser


fun main() {

    val x = (0..5)
    val y = x.map { it * it }

    val trace = Trace.invoke(x, y) {
        name = "sin"
        /* The hover text definition is currently not supported.
         * We are applying it directly to configuration.
         * It is still observable in the same way as other properties but is not type safe.
         */
        properties["text"] = x.map { "label for  $it".asValue() }
    }

    val plot = Plotly.plot {
        traces(trace)
        layout {
            title = "Plot with labels"
            xaxis {
                title = "x axis name"
                configure {
                    "rangebreaks" putIndexed listOf(
                        Meta {
                            "values" put ListValue(2.0, 3.0)
                        }
                    )
                }
            }
            yaxis { title = "y axis name" }
        }
    }

    plot.openInBrowser()
}
