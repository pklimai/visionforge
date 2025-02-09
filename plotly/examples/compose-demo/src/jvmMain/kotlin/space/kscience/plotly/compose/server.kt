package space.kscience.plotly.compose

import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import space.kscience.plotly.*
import space.kscience.plotly.models.Scatter
import space.kscience.plotly.models.Trace
import space.kscience.visionforge.html.makeString
import space.kscience.visionforge.plotly.plotlyPage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


public fun Scatter(
    xs: Any,
    ys: Any? = null,
    zs: Any? = null,
    block: Trace.() -> Unit,
): Scatter = Scatter().apply {
    x.set(xs)
    if (ys != null) y.set(ys)
    if (zs != null) z.set(zs)
    block()
}


fun staticPlot(): String = Plotly.page {
    val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
    val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
    val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
    val trace1 = Scatter(x, y1) {
        name = "sin"
    }
    val trace2 = Scatter(x, y2) {
        name = "cos"
    }
    vision {
        plotly(config = PlotlyConfig { responsive = true }) {//static plot
            traces(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis.title = "x axis name"
                yaxis { title = "y axis name" }
            }
        }
    }
}.makeString()

fun CoroutineScope.servePlots(scale: StateFlow<Number>): EmbeddedServer<*, *> = embeddedServer(CIO, port = 7777) {

    val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()

    plotlyPage("Static") {
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()
        val trace1 = Scatter(x, y1) {
            name = "sin"
        }
        val trace2 = Scatter(x, y2) {
            name = "cos"
        }

        plot {
            traces(trace1, trace2)
            layout {
                title = "First graph, row: 1, size: 8/12"
                xaxis.title = "x axis name"
                yaxis { title = "y axis name" }
            }
        }
    }

    plotlyPage("Dynamic") {
        val y = x.map { sin(2.0 * PI * it) }

        val trace = Scatter(x, y) { name = "sin" }


        plot {
            traces(trace)
            layout {
                title = "Dynamic plot"
                xaxis.title = "x axis name"
                yaxis.title = "y axis name"
            }

            launch {
                var time: Long = 0
                while (isActive) {
                    delay(10)
                    time += 10
                    val frequency = scale.value.toDouble()
                    val dynamicY = x.map { sin(2.0 * PI * frequency * (it + time.toDouble() / 1000.0)) }
                    //trace.y.numbers = dynamicY
                    data[0].y.numbers = dynamicY
                    layout {
                        xaxis.title = "x axis name (t = $time)"
                    }
                }
            }
        }
    }
}

