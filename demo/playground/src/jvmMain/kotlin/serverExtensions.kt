package space.kscience.visionforge.examples

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import kotlinx.html.unsafe
import space.kscience.dataforge.context.Context
import space.kscience.plotly.PlotlyPlugin
import space.kscience.visionforge.html.*
import space.kscience.visionforge.markup.MarkupPlugin
import space.kscience.visionforge.server.VisionRoute
import space.kscience.visionforge.server.close
import space.kscience.visionforge.server.openInBrowser
import space.kscience.visionforge.server.visionPage
import space.kscience.visionforge.solid.Solids
import space.kscience.visionforge.tables.TableVisionPlugin
import space.kscience.visionforge.visionManager
import java.awt.Desktop
import java.nio.file.Path

val context = Context("playground") {
    plugin(Solids)
    plugin(PlotlyPlugin)
    plugin(MarkupPlugin)
    plugin(TableVisionPlugin)
}


public fun makeVisionFile(
    path: Path? = null,
    title: String = "VisionForge page",
    resourceLocation: ResourceLocation = ResourceLocation.SYSTEM,
    show: Boolean = true,
    content: HtmlVisionFragment,
): Unit {
    val actualPath = VisionPage(context.visionManager, content = content).makeFile(path) { actualPath ->
        mapOf(
            "title" to VisionPage.title(title),
            "playground" to VisionPage.importScriptHeader(
                "js/visionforge-playground.js",
                resourceLocation,
                actualPath
            ),
            "playground-style" to VisionPage.styleHeader {
                unsafe {
                    +".visionforge-output { height:100vh; }"
                }
            }
        )
    }
    if (show) Desktop.getDesktop().browse(actualPath.toFile().toURI())
}

public suspend fun serve(
    title: String = "VisionForge page",
    show: Boolean = true,
    routeConfiguration: VisionRoute.() -> Unit = {},
    content: HtmlVisionFragment,
) {
    val server = embeddedServer(CIO, port = 7779) {
        routing {
            staticResources("", null, null)
        }

        visionPage(
            context.visionManager,
            VisionPage.scriptHeader("js/visionforge-playground.js") {
                defer = true
            },
            VisionPage.title(title),
            routeConfiguration = routeConfiguration,
            visionFragment = content
        )
    }.start(false)

    if (show) {
        server.openInBrowser()
    }

    println("Enter 'exit' to close server")
    while (readlnOrNull() != "exit") {
        //
    }

    server.close()
}

//@DFExperimental
//public fun Context.makeVisionFile(
//    vision: Vision,
//    path: Path? = null,
//    title: String = "VisionForge page",
//    resourceLocation: ResourceLocation = ResourceLocation.SYSTEM,
//    show: Boolean = true,
//): Unit = makeVisionFile({ vision(vision) }, path, title, resourceLocation, show)
