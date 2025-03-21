package space.kscience.plotly.models.geo

import space.kscience.plotly.Plot
import space.kscience.plotly.models.TraceType

public class ChoroplethMapBox : GeoTrace() {
    init {
        type = TraceType.choroplethmapbox
    }

    public companion object
}

public inline fun Plot.choroplethMapBox(block: ChoroplethMapBox.() -> Unit): ChoroplethMapBox {
    val trace = ChoroplethMapBox().apply(block)
    traces(trace)
    return trace
}