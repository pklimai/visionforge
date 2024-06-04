package bar

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Bar
import space.kscience.plotly.models.Orientation
import kotlin.random.Random


/**
 * - Horizontal bar chart
 * - Use css named color palette
 * - Change fontsize of the ticks
 * - Add space between axis and ticklabels (set tickcolor to white)
 */
fun main() {
    val colors = listOf("DarkOliveGreen", "OliveDrab", "YellowGreen", "GreenYellow", "Yellow", "PeachPuff",
            "Pink", "HotPink", "DeepPink", "MediumVioletRed", "Purple", "RebeccaPurple", "MediumBlue", "Blue",
            "DodgerBlue", "SteelBlue", "LightSlateGrey", "SlateGrey", "DarkSlateGrey", "Black")
    val length = mutableListOf<Double>()
    for (i in colors.indices) {
        length.add(Random.nextDouble())
    }

    val bar = Bar {
        y.strings = colors
        x.numbers = length
        orientation = Orientation.h
        marker {
            colors(colors)
        }
    }

    val plot = Plotly.plot {
        traces(bar)

        layout {
            title = "What an Awesome Plot!"
            height = 700
            width = 1200
            margin {
                l = 160
            }

            xaxis {
                tickfont {
                    size = 14
                }
            }
            yaxis {
                tickfont {
                    size = 16
                }
                ticklen = 4
                tickcolor("white")
            }
        }
    }
    plot.makeFile()
}