package space.kscience.visionforge.plotly

import space.kscience.dataforge.misc.DFExperimental
import space.kscience.plotly.PlotlyPlugin
import space.kscience.visionforge.html.runVisionClient


@DFExperimental
public fun main(): Unit = runVisionClient {
    plugin(PlotlyPlugin)
}