import space.kscience.dataforge.meta.ListValue
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.asValue
import space.kscience.dataforge.meta.configure
import space.kscience.dataforge.meta.invoke
import space.kscience.dataforge.meta.set
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke


fun main() {

    val x = (0..5)
    val y = x.map { it * it }

    val trace = Trace.invoke(x, y) {
        name = "sin"
        /* The hover text definition is currently not supported.
         * We are applying it directly to configuration.
         * It is still observable in the same way as other properties but is not type safe.
         */
        meta["text"] = x.map { "label for  $it".asValue() }
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

    plot.makeFile()
}
