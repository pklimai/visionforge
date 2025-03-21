package ru.mipt.npm.sat


import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import kotlinx.coroutines.*
import kotlinx.html.div
import kotlinx.html.h1
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.request
import space.kscience.dataforge.meta.Null
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.Colors
import space.kscience.visionforge.html.VisionPage
import space.kscience.visionforge.html.meta
import space.kscience.visionforge.server.close
import space.kscience.visionforge.server.openInBrowser
import space.kscience.visionforge.server.visionPage
import space.kscience.visionforge.solid.*
import space.kscience.visionforge.solid.specifications.Canvas3DOptions
import space.kscience.visionforge.three.threeJsHeader
import kotlin.random.Random


@Suppress("ExtractKtorModule")
suspend fun main() {
    val satContext = Context("sat") {
        plugin(Solids)
    }

    val solids = satContext.request(Solids)

    //Create a geometry
    val sat = solids.visionOfSatellite(ySegments = 3).apply {
        ambientLight {
            color(Colors.white)
            intensity = 3.0
        }
    }
    val server = embeddedServer(CIO, port = 7777) {
        routing {
            staticResources("", null, null)
        }

        visionPage(
            solids.visionManager, VisionPage.threeJsHeader,
            VisionPage.styleSheetHeader("css/styles.css")
        ) {
            div("flex-column") {
                h1 { +"Satellite detector demo" }
                vision {
                    meta(Canvas3DOptions {
                        controls.enabled = false
                    })
                    sat
                }
            }
        }

    }.start(false)

    server.openInBrowser()

    @OptIn(DelicateCoroutinesApi::class)
    GlobalScope.launch {
        while (isActive) {
            val randomLayer = Random.nextInt(1, 11)
            val randomI = Random.nextInt(1, 4)
            val randomJ = Random.nextInt(1, 4)
            val target = Name.parse("layer[$randomLayer].segment[$randomI,$randomJ]")
            val targetVision = sat[target] as Solid
            targetVision.color("red")
            delay(1000)
            //use to ensure that color is cleared
            targetVision.color.value = Null
            delay(500)
        }
    }

    println("Enter 'exit' to close server")
    while (readLine() != "exit") {
        //
    }

    server.close()

}