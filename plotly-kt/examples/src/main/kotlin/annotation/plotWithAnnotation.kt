package annotation

import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.models.scatter
import space.kscience.plotly.openInBrowser
import space.kscience.plotly.text

fun main() {
    val plot = Plotly.plot {
        scatter {
            x(2, 3, 4, 5)
            y(10, 15, 13, 17)
            mode = ScatterMode.lines
        }

        text {
            position(2, 10)
            text = "start"
        }

        text {
            position(5, 17)
            text = "finish"
        }

        layout {
            title = "Plot with annotation"
        }
    }

    plot.openInBrowser()
}
