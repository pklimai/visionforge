package pie

import space.kscience.dataforge.meta.invoke
import space.kscience.plotly.Plotly
import space.kscience.plotly.fragment
import space.kscience.plotly.makeFile
import space.kscience.plotly.models.Pie
import space.kscience.plotly.plot


fun main() {
    val donut1 = Pie {
        values(listOf<Number>(16, 15, 12, 6, 5, 4, 42))
        labels(listOf("US", "China", "European Union", "Russian Federation", "Brazil", "India", "Rest of World"))
        hole = 0.4
    }

    val donut2 = Pie {
        values(listOf<Number>(27, 11, 25, 8, 1, 3, 25))
        labels(listOf("US", "China", "European Union", "Russian Federation", "Brazil", "India", "Rest of World"))
        hole = 0.4
    }

    Plotly.fragment {
        plot {
            traces(donut1)

            layout {
                width = 600
                height = 600
                title = "GHG Emissions"
            }
        }

        plot {
            traces(donut2)

            layout {
                width = 600
                height = 600
                title = "CO2"
            }
        }
    }.makeFile()
}