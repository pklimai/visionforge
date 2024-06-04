package histogram

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.histogram
import space.kscience.plotly.makeFile
import java.util.*


/**
 * - basic histogram
 * - change size of gap between bins
 * - change font color and size of title
 * - change font size of axis title
 */
fun main() {
    val rnd = Random()
    val values = List(500) { rnd.nextDouble() }

    val plot = Plotly.plot {
        histogram {
            x.numbers = values
            name = "Random data"
        }

        layout {
            bargap = 0.1
            title {
                text = "Basic Histogram"
                font {
                    size = 20
                    color("black")
                }
            }
            xaxis {
                title {
                    text = "Value"
                    font {
                        size = 16
                    }
                }
            }
            yaxis {
                title {
                    text = "Count"
                    font {
                        size = 16
                    }
                }
            }
        }
    }

    plot.makeFile()
}