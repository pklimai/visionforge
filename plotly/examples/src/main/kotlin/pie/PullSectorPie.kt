package pie

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.TextInfo
import space.kscience.plotly.models.TextPosition
import space.kscience.plotly.pie


/**
 * - Pie chart with pulled out sector
 * - Change start angle of pie
 * - Show labels outside the pie
 */
fun main() {
    val labels = listOf("Python", "C++", "Ruby", "Java")
    val values = listOf(215, 130, 245, 210)
    val pullSectors = listOf<Number>(0.1, 0, 0, 0)

    val plot = Plotly.plot {
        pie {
            labels(labels)
            values(values)
            pullList = pullSectors
            rotation = -45
            textposition = TextPosition.outside
            textinfo = TextInfo.`label+percent`
            textfont {
                size = 16
            }
            showlegend = false
        }

        layout {
            height = 600
            width = 700
            title = "Pull Sector Pie Chart"
        }
    }
    plot.makeFile()
}