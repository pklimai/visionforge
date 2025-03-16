package space.kscience.plotly.models.geo

import space.kscience.plotly.Plot
import space.kscience.plotly.models.TraceType
import space.kscience.plotly.models.TraceValues

public class ScatterMapBox : GeoTrace() {
    init {
        type = TraceType.scattermapbox
    }

    public val ids: TraceValues by axis
    public val lat: TraceValues by axis
    public val lon: TraceValues by axis

    public companion object
}

public inline fun Plot.scattermapbox(block: ScatterMapBox.() -> Unit): ScatterMapBox {
    val trace = ScatterMapBox().apply(block)
    traces(trace)
    return trace
}