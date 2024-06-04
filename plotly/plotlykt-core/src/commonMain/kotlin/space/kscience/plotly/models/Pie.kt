package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.asName
import space.kscience.plotly.numberInRange
import kotlin.js.JsName

public enum class PieDirection {
    clockwise,
    counterclockwise
}

public enum class TextInfo {
    label,
    text,
    value,
    percent,
    none,

    @JsName("labelText")
    `label+text`,

    @JsName("labelValue")
    `label+value`,

    @JsName("labelPercent")
    `label+percent`,

    @JsName("textValue")
    `text+value`,

    @JsName("textPercent")
    `text+percent`,

    @JsName("valuePercent")
    `value+percent`,

    @JsName("labelTextValue")
    `label+text+value`,

    @JsName("labelTextPercent")
    `label+text+percent`,

    @JsName("labelValuePercent")
    `label+value+percent`,

    @JsName("textValuePercent")
    `text+value+percent`,

    @JsName("labelTextValuePercent")
    `label+text+value+percent`
}

public class Pie : Trace() {
    init {
        type = TraceType.pie
    }

    /**
     * Sets the fraction of larger radius to pull the sectors out from the center.
     * This is a constant to pull all slices apart from each other.
     * Default: 0.
     */
    public var pull: Number? by number()

    /**
     * Sets the fraction of larger radius to pull the sectors out from the center.
     * This is an array to highlight one or more slices.
     */
    public var pullList: List<Number> by numberList(key = "pull".asName())

    /**
     * Specifies the direction at which succeeding sectors follow one another.
     */
    public var direction: PieDirection by enum(PieDirection.counterclockwise)

    /**
     * Sets the fraction of the radius to cut out of the pie.
     * Use this to make a donut chart. Default: 0.
     */
    public var hole: Number by numberInRange(0.0..1.0)

    /**
     * Instead of the first slice starting at 12 o'clock, rotate to some other angle.
     * Default: 0.
     */
    public var rotation: Number by numberInRange(-360.0..360.0)

    /**
     * Determines whether or not the sectors are reordered from largest to smallest.
     * Default: true.
     */
    public var sort: Boolean? by boolean()

    /**
     * Sets the label step. See `label0` for more info.
     * Default: 1.
     */
    public var dlabel: Number? by number()

    /**
     * Alternate to `labels`. Builds a numeric set of labels.
     * Use with `dlabel` where `label0` is the starting label and `dlabel` the step.
     * Default: 0.
     */
    public var label0: Number? by number()

    /**
     * Determines which trace information appear on the graph.
     */
    public var textinfo: TextInfo by enum(TextInfo.percent)

    public companion object : SchemeSpec<Pie>(::Pie)
}