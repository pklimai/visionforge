package scatter

import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.*


/**
 * - Categorical dot scatter plot
 * - Use different scatter modes
 * - Choose marker's symbol and size
 * - Change background plot color
 */
fun main() {
    val country = listOf("Switzerland (2011)", "Chile (2013)", "Japan (2014)", "United States (2012)",
            "Slovenia (2014)", "Canada (2011)", "Poland (2010)", "Estonia (2015)", "Luxembourg (2013)", "Portugal (2011)")

    val votingPop = listOf<Number>(40, 45.7, 52, 53.6, 54.1, 54.2, 54.5, 54.7, 55.1, 56.6)

    val regVoters = listOf<Number>(49.1, 42, 52.7, 84.3, 51.7, 61.1, 55.3, 64.2, 91.1, 58.9)

    val trace1 = Scatter {
        x.numbers = votingPop
        y.strings = country
        mode = ScatterMode.markers
        name = "Percent of estimated voting age population"
        marker {
            color("rgba(156, 165, 196, 0.95)")
            line {
                color("rgba(156, 165, 196, 1.0)")
                width = 1
            }
            symbol = Symbol.circle
            size = 16
        }
    }

    val trace2 = Scatter {
        x.numbers = regVoters
        y.strings = country
        mode = ScatterMode.markers
        name = "Percent of estimated registered voters"
        marker {
            color("rgba(204, 204, 204, 0.95)")
            line {
                color("rgba(217, 217, 217, 1.0)")
                width = 1
            }
            symbol = Symbol.circle
            size = 16
        }
    }

    val plot = Plotly.plot {
        traces(trace1, trace2)

        layout {
            title = "Votes cast for ten lowest voting age population in OECD countries"
            xaxis {
                showgrid = false
                showline = true
                linecolor("rgb(102, 102, 102)")
                title {
                    font { color("rgb(204, 204, 204)") }
                }
                tickfont { color("rgb(102, 102, 102)") }

                autotick = false
                dtick = Value.of(10)
                ticks = Ticks.outside
                tickcolor("rgb(102, 102, 102)")
            }

            margin { l = 140; r = 40; b = 50; t = 80 }
            legend {
                font { size = 10 }
                yanchor = YAnchor.middle
                xanchor = XAnchor.right
            }
            width = 600
            height = 600
            paper_bgcolor("rgb(254, 247, 234)")
            plot_bgcolor("rgb(254, 247, 234)")
            hovermode = HoverMode.closest
        }
    }
    plot.makeFile()
}