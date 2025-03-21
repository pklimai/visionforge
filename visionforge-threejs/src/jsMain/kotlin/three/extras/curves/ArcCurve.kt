@file:JsModule("three")


package three.extras.curves

external class ArcCurve(
    aX: Number = definedExternally,
    aY: Number = definedExternally,
    xRadius: Number = definedExternally,
    yRadius: Number = definedExternally,
    aStartAngle: Number = definedExternally,
    aEndAngle: Number = definedExternally,
    aClockwise: Number = definedExternally
) : EllipseCurve {

    override fun clone(): ArcCurve
    fun copy(curve: ArcCurve): ArcCurve

}

