package misc

import space.kscience.plotly.Plotly
import space.kscience.plotly.models.bar
import space.kscience.plotly.openInBrowser

fun main() {
    val plot = Plotly.plot {
        bar {
            x("giraffes")
            y(20)
        }
    }
    plot.openInBrowser()
}