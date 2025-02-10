import space.kscience.dataforge.misc.DFExperimental
import space.kscience.plotly.PlotlyJSPlugin
import space.kscience.visionforge.html.runVisionClient
import space.kscience.visionforge.jupyter.VFNotebookClient
import space.kscience.visionforge.markup.MarkupPlugin
import space.kscience.visionforge.solid.three.ThreePlugin
import space.kscience.visionforge.tables.TableVisionJsPlugin

@DFExperimental
fun main() = runVisionClient {
    plugin(ThreePlugin)
    plugin(PlotlyJSPlugin)
    plugin(MarkupPlugin)
    plugin(TableVisionJsPlugin)
    plugin(VFNotebookClient)
}