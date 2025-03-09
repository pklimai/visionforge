package contour

import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Contour
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * - simple contour plot without level lines
 * - transpose axis
 * - change size of the plot
 * - change color scale
 */
fun main() {
    val size = 25
    val x1 = (0..size).map { it.toDouble() / 5 }
    val y1 = (0..size).map { it.toDouble() / 5 }
    val z = mutableListOf<MutableList<Double>>()

    for (i in y1.indices) {
        z.add(MutableList(size + 1) { 0.0 })
    }

    for (i in x1.indices) {
        for (j in y1.indices) {
            z[i][j] = sin(x1[i]).pow(10) + cos(10 + y1[j] * x1[i]) * cos(x1[i])
        }
    }

    val contour = Contour {
        x.numbers = x1
        y.numbers = y1
        z(z)

        transpose = true
        colorscale = Value.of("Portland")
        reversescale = true

        contours {
            showlines = false
        }
    }

    val plot = Plotly.plot {
        traces(contour)

        layout {
            width = 1000
            height = 500
            title = "Colored Contour Plot"
        }
    }

    plot.makeFile()
}
