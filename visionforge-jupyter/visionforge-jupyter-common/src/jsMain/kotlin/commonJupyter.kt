package space.kscience.visionforge.gdml.jupyter

import space.kscience.plotly.PlotlyJsPlugin
import space.kscience.visionforge.html.runVisionClient
import space.kscience.visionforge.jupyter.VFNotebookClient
import space.kscience.visionforge.markup.MarkupJsPlugin
import space.kscience.visionforge.solid.three.ThreePlugin
import space.kscience.visionforge.tables.TableVisionJsPlugin

public fun main(): Unit = runVisionClient {
    plugin(ThreePlugin)
    plugin(PlotlyJsPlugin)
    plugin(MarkupJsPlugin)
    plugin(TableVisionJsPlugin)
    plugin(VFNotebookClient)
}

