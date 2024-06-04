package box

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Box
import space.kscience.plotly.models.BoxPoints
import kotlin.random.Random


/**
 * - Basic Box plot with all points
 */
fun main() {
    val size = 50
    val y0 = mutableListOf<Double>()
    val y1 = mutableListOf<Double>()

    for (i in 0 until size) {
        y0.add(Random.nextDouble())
        y1.add(Random.nextDouble())
    }

    val trace1 = Box {
        y.set(y0)
        boxpoints = BoxPoints.all
        jitter = 0.3
        pointpos = -1.8
    }

    val trace2 = Box {
        y.set(y1)
        boxpoints = BoxPoints.all
        jitter = 0.3
        pointpos = -1.8
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Basic Box Plot"
        }
    }
    plot.makeFile()
}