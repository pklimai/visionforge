package box

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Box
import space.kscience.plotly.models.BoxMean
import space.kscience.plotly.palettes.Xkcd

fun main() {
    val y1 = listOf(2.37, 2.16, 4.82, 1.73, 1.04, 0.23, 1.32, 2.91, 0.11, 4.51, 0.51,
            3.75, 1.35, 2.98, 4.50, 0.18, 4.66, 1.30, 2.06, 1.19)

    val trace1 = Box {
        y.numbers = y1
        name = "Only Mean"
        marker {
            color(Xkcd.CERULEAN)
        }
        boxmean = BoxMean.`true`
    }

    val trace2 = Box {
        y.numbers = y1
        name = "Mean and Standard Deviation"
        marker {
            color(Xkcd.BLUE_VIOLET)
        }
        boxmean = BoxMean.sd
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Box Plot Styling Mean and Standard Deviation"
        }
    }
    plot.makeFile()
}