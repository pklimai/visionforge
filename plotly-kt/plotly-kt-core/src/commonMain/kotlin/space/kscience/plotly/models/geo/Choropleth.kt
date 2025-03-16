package space.kscience.plotly.models.geo

import space.kscience.plotly.Plot
import space.kscience.plotly.models.TraceType
import kotlin.js.JsName

public enum class LocationMode{
    @JsName("iso3")
    `ISO-3`,
    @JsName("usa")
    `USA-states`,
    @JsName("country")
    `country names`,
    @JsName("geojson")
    `geojson-id`
}

public class Choropleth : GeoTrace() {
    init {
        type = TraceType.choropleth
    }

    public companion object
}

public inline fun Plot.choropleth(block: Choropleth.() -> Unit): Choropleth {
    val trace = Choropleth().apply(block)
    traces(trace)
    return trace
}