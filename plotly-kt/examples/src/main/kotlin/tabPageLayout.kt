import kotlinx.html.*
import space.kscience.plotly.*
import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import space.kscience.plotly.palettes.T10
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.VisionPage
import space.kscience.visionforge.html.appendTo
import space.kscience.visionforge.html.openInBrowser
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public val cdnBootstrap: HtmlFragment = HtmlFragment {
    script {
        src = "https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity = "sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        attributes["crossorigin"] = "anonymous"
    }
    script {
        src = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"
        integrity = "sha384-1CmrxMRARb6aLqgBO7yyAxTOQE2AKb9GfXnEo760AUcUmFx3ibVJJAzGytlQcNXd"
        attributes["crossorigin"] = "anonymous"
    }
    link {
        rel = "stylesheet"
        href = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
        attributes["integrity"] = "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
        attributes["crossorigin"] = "anonymous"
    }
}


public class PlotTabs {
    public data class Tab(val title: String, val id: String, val content: HtmlFragment)

    private val _tabs = ArrayList<Tab>()
    public val tabs: List<Tab> get() = _tabs

    public fun tab(title: String, id: String = title, fragment: HtmlFragment) {
        _tabs.add(Tab(title, id, fragment))
    }
}

public fun Plotly.tabs(tabsID: String = "tabs", block: PlotTabs.() -> Unit): VisionPage {
    val grid = PlotTabs().apply(block)

    return plugin.visionManager.VisionPage(cdnBootstrap, cdnPlotlyHeader) {
        ul("nav nav-tabs") {
            role = "tablist"
            id = tabsID
            grid.tabs.forEachIndexed { index, tab ->
                li("nav-item") {
                    a(classes = "nav-link") {
                        if (index == 0) {
                            classes = classes + "active"
                        }
                        id = "${tab.id}-tab"
                        attributes["data-toggle"] = "tab"
                        href = "#${tab.id}"
                        role = "tab"
                        attributes["aria-controls"] = tab.id
                        attributes["aria-selected"] = "true"
                        +tab.title
                    }
                }
            }
        }
        div("tab-content") {
            id = "$tabsID-content"
            grid.tabs.forEachIndexed { index, tab ->
                div("tab-pane fade") {
                    if (index == 0) {
                        classes = classes + setOf("show", "active")
                    }
                    id = tab.id
                    role = "tabpanel"
                    attributes["aria-labelledby"] = "${tab.id}-tab"
                    tab.content.appendTo(consumer)
                }
            }
        }
    }
}


@Suppress("DEPRECATION")
@UnstablePlotlyAPI
fun main() {

    val x = (0..100).map { it.toDouble() / 100.0 }
    val y1 = x.map { sin(2.0 * PI * it) }
    val y2 = x.map { cos(2.0 * PI * it) }

    val trace1 = Trace(x, y1) {
        name = "sin"
        marker.color(T10.BLUE)

    }

    val trace2 = Trace(x, y2) {
        name = "cos"
        marker.color(T10.ORANGE)

    }

    val responsive = PlotlyConfig {
        responsive = true
    }

    val page = Plotly.tabs {

        tab("First") {
            staticPlot(config = responsive) {
                traces(trace1)
                layout {
                    title = "First graph"
                    xaxis.title = "x axis name"
                    xaxis.title = "y axis name"
                }
            }
        }
        tab("Second") {
            staticPlot(config = responsive) {
                traces(trace2)
                layout {
                    title = "Second graph"
                    xaxis.title = "x axis name"
                    xaxis.title = "y axis name"
                }
            }
        }
    }

    page.openInBrowser()
}
