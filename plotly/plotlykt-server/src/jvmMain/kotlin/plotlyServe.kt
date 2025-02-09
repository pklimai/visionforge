package space.kscience.visionforge.plotly

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.plotly.Plotly
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.HtmlVisionFragment
import space.kscience.visionforge.html.VisionPage
import space.kscience.visionforge.server.VisionRoute
import space.kscience.visionforge.server.visionPage


public val VisionPage.Companion.plotlyKtHeader: HtmlFragment get() = scriptHeader("js/plotly-kt-server.js")

/**
 * Add a page with plotly resources loaded
 */
public fun Application.plotlyPage(
    route: String = "/",
    title: String = "VisionForge Plotly page",
    routeConfiguration: VisionRoute.() -> Unit = {},
    content: HtmlVisionFragment
) {
    routing {
        staticResources("js", "js", null)
    }

    visionPage(
        Plotly.plugin.visionManager,
        VisionPage.title(title), VisionPage.plotlyKtHeader,
        route = route,
        routeConfiguration = routeConfiguration,
        visionFragment = content
    )
}

@DFExperimental
public fun Plotly.serveSinglePage(
    title: String = "VisionForge Plotly page",
    port: Int = 7777,
    routeConfiguration: VisionRoute.() -> Unit = {},
    content: HtmlVisionFragment
): EmbeddedServer<*, *> = embeddedServer(CIO, port = port) {
    plotlyPage(title = title, routeConfiguration = routeConfiguration, content = content)

}.start(false)