package space.kscience.plotly

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonArray
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.ContextAware
import space.kscience.dataforge.context.request
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.VisionBuilder
import space.kscience.visionforge.VisionManager
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.HtmlVisionFragment
import space.kscience.visionforge.html.VisionOutput
import space.kscience.visionforge.html.VisionPage
import kotlin.js.JsName

/**
 * A namespace for utility functions
 */
@JsName("PlotlyKt")
public object Plotly : ContextAware {
    public const val VERSION: String = "2.24.1"

    public const val PLOTLY_CDN: String = "https://cdn.plot.ly/plotly-${VERSION}.min.js"
    //"https://cdnjs.cloudflare.com/ajax/libs/plotly.js/${VERSION}/plotly.min.js"

    public val coordinateNames: List<String> = listOf(
        "x", "y", "z", "text", "hovertext", "close", "high", "low", "open", "locations", "lon", "lat", "ids"
    )

    override val context: Context = Context("Plotly") {
        plugin(PlotlyPlugin)
    }

    public val plugin: PlotlyPlugin = context.request(PlotlyPlugin)

    public inline fun plot(block: Plot.() -> Unit): Plot = Plot().apply(block)
}

/**
 * Convert any type-safe configurator to json string
 */
public fun Scheme.toJsonString(): String = meta.toJson().toString()

private fun List<Scheme>.toJson(): JsonArray = buildJsonArray {
    forEach { add(it.meta.toJson()) }
}

/**
 * Convert list of type-safe configurators to json array string
 */
public fun List<Scheme>.toJsonString(): String = toJson().toString()

@RequiresOptIn("Unstable API subjected to change in future releases", RequiresOptIn.Level.WARNING)
public annotation class UnstablePlotlyAPI

@RequiresOptIn("This Plotly JS API is not fully supported. Use it at your own risk.", RequiresOptIn.Level.ERROR)
public annotation class UnsupportedPlotlyAPI

public class PlotlyConfig : Scheme() {
    public var editable: Boolean? by boolean()
    public var showEditInChartStudio: Boolean? by boolean()
    public var plotlyServerURL: String? by string()
    public var responsive: Boolean? by boolean()
    public var imageFormat: String? by string(Name.parse("toImageButtonOptions.format"))

    public fun withEditorButton() {
        showEditInChartStudio = true
        plotlyServerURL = "https://chart-studio.plotly.com"
    }

    public fun saveAsSvg() {
        imageFormat = "svg"
    }

    override fun toString(): String = toJsonString()

    public companion object : SchemeSpec<PlotlyConfig>(::PlotlyConfig)
}

/**
 * Embed a dynamic plotly plot in a vision
 */
@VisionBuilder
public inline fun VisionOutput.plotly(
    config: PlotlyConfig = PlotlyConfig(),
    block: Plot.() -> Unit,
): Plot {
    requirePlugin(PlotlyPlugin)
    meta = config.meta
    return Plotly.plot(block)
}

public fun Plotly.page(
    pageHeaders: Map<String, HtmlFragment> = emptyMap(),
    content: HtmlVisionFragment,
): VisionPage = VisionPage(context.request(VisionManager), pageHeaders, content)