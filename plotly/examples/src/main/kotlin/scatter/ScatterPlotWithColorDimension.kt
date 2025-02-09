package scatter

import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.models.Scatter
import space.kscience.plotly.models.ScatterMode
import space.kscience.plotly.models.invoke
import space.kscience.plotly.openInBrowser


/**
 * - Scatter plot only with markers
 * - Use numbers as a color list
 */
fun main() {
    val scatter = Scatter {
        y.set(List(40) { 5.0 })
        mode = ScatterMode.markers
        marker {
            size = 40
            colors(List(40) { Value.of(it) })
        }
    }

    val plot = Plotly.plot {
        traces(scatter)

        layout {
            title = "Scatter plot with color dimension"
        }
    }
    plot.openInBrowser()
}
