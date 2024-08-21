package space.kscience.visionforge.plotly

import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import kotlinx.coroutines.launch
import kotlinx.html.title
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.plotly.Plot
import space.kscience.plotly.Plotly
import space.kscience.plotly.PlotlyConfig
import space.kscience.plotly.PlotlyPlugin
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.HtmlVisionFragment
import space.kscience.visionforge.html.VisionPage
import space.kscience.visionforge.server.visionPage
import space.kscience.visionforge.setAsRoot


public val VisionPage.Companion.plotlyKtHeader: HtmlFragment get() = scriptHeader("js/plotly-kt-server.js")


@DFExperimental
public fun Plotly.servePage(
    title: String = "VisionForge Plotly page",
    port: Int = 7777,
    visionPage: HtmlVisionFragment
): ApplicationEngine = embeddedServer(CIO, port = port) {
    routing {
        staticResources("js", "js", null)
    }

    visionPage(
        plugin.visionManager,
        HtmlFragment { title(title) },
        VisionPage.plotlyKtHeader,
        visionFragment = visionPage
    )

}.start(false)


/**
 * Serve a page containing a single plot
 */
@DFExperimental
public fun Plotly.serve(
    title: String = "VisionForge Plotly page",
    port: Int = 7777,
    config: PlotlyConfig = PlotlyConfig(),
    makePlot: suspend Plot.() -> Unit,
): ApplicationEngine = embeddedServer(CIO, port = port) {
    routing {
        staticResources("js", "js", null)
    }

    val plot: Plot = Plot()
    plot.setAsRoot(plugin.visionManager)

    launch {
        makePlot(plot)
    }

    visionPage(
        plugin.visionManager,
        HtmlFragment { title(title) },
        VisionPage.plotlyKtHeader,
    ) {
        vision {
            requirePlugin(PlotlyPlugin)
            meta = config.meta
            plot
        }
    }

}.start(false)

