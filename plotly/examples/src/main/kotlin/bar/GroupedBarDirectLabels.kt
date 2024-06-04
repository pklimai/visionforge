package bar

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Bar
import space.kscience.plotly.models.TextPosition


/**
 * - Grouped bar chart
 * - Use rgb(a) color palette
 * - Hide hoverinfo & legend
 * - Show text on the bars
 */
fun main() {
    val xValues = listOf("Product A", "Product B", "Product C")
    val yValues = listOf(20, 14, 23)
    val yValues2 = listOf(24, 16, 20)

    val trace1 = Bar {
        x.strings = xValues
        y.numbers = yValues
        text.strings = yValues.map { it.toString() }
        textposition = TextPosition.auto
        hoverinfo = "none"
        opacity = 0.5
        showlegend = false
        marker {
            color("rgb(158, 202, 225)")
            line {
                color("rgb(8, 48, 107)")
                width = 2
            }
        }
    }

    val trace2 = Bar {
        x.strings = xValues
        y.numbers = yValues2
        text.strings = yValues2.map { it.toString() }
        textposition = TextPosition.auto
        hoverinfo = "none"
        showlegend = false
        marker {
            color("rgba(58, 200, 225, 0.5)")
            line {
                color("rgb(8, 48, 107)")
                width = 2
            }
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "January 2013 Sales Report"
        }
    }
    plot.makeFile()
}