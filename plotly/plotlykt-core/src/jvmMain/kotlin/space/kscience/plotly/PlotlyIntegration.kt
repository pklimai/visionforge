package space.kscience.plotly

import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import space.kscience.plotly.models.Trace
import space.kscience.visionforge.html.HtmlFragment

public object PlotlyJupyterConfiguration {
    public var legacyMode: Boolean = false

    /**
     * Switch plotly renderer to the legacy notebook mode (Jupyter classic)
     */
    public fun notebook(): HtmlFragment {
        legacyMode = true
        return HtmlFragment {
            div {
                style = "color: blue;"
                +"Plotly notebook integration switched into the notebook mode."
            }
        }
    }

    public fun lab(): HtmlFragment {
        legacyMode = false
        return HtmlFragment {
            div {
                style = "color: blue;"
                +"Plotly notebook integration switched into the lab mode."
            }
        }
    }
}

/**
 * Global plotly jupyter configuration
 */
public val Plotly.jupyter: PlotlyJupyterConfiguration
    get() = PlotlyJupyterConfiguration

public class PlotlyIntegration : JupyterIntegration() {

    private fun renderPlot(plot: Plot) = if (PlotlyJupyterConfiguration.legacyMode) {
        HTML(plot.toHTMLPage(), true)
    } else {
        HTML(createHTML().div { consumer.staticPlot(plot, PlotlyConfig { responsive = true }) }, false)
    }


    override fun Builder.onLoaded() {

        resources {
            js("plotly-kt") {
                url(Plotly.PLOTLY_CDN)
                classPath("js/plotly-kt.js")
            }
        }

        repositories("https://repo.kotlin.link")

        import(
            "space.kscience.plotly.*",
            "space.kscience.plotly.models.*",
            "space.kscience.dataforge.meta.*",
            "kotlinx.html.*"
        )

        import("space.kscience.plotly.jupyter")

        render<HtmlFragment> {
            HTML(it.toString())
        }

        render<Plot> { plot ->
            renderPlot(plot)
        }

        render<Trace> { trace ->
            renderPlot(
                Plotly.plot {
                    traces(trace)
                }
            )
        }

    }

}
