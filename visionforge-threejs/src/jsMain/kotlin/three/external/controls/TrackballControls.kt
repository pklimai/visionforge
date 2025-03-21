@file:JsModule("three/examples/jsm/controls/TrackballControls.js")


package three.external.controls

import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import three.cameras.Camera
import three.core.EventDispatcher
import three.math.Vector3

external interface `T$0` {
    var left: Number
    var top: Number
    var width: Number
    var height: Number
}

external open class TrackballControls(
    `object`: Camera,
    domElement: Node = definedExternally /* null */
) : EventDispatcher {
    open var `object`: Camera
    open var domElement: HTMLElement
    open var enabled: Boolean
    open var screen: `T$0`
    open var rotateSpeed: Number
    open var zoomSpeed: Number
    open var panSpeed: Number
    open var noRotate: Boolean
    open var noZoom: Boolean
    open var noPan: Boolean
    open var noRoll: Boolean
    open var staticMoving: Boolean
    open var dynamicDampingFactor: Number
    open var minDistance: Number
    open var maxDistance: Number
    open var keys: Array<Number>
    open var target: Vector3
    open var position0: Vector3
    open var target0: Vector3
    open var up0: Vector3
    open fun update(): Unit
    open fun reset(): Unit
    open fun dispose(): Unit
    open fun checkDistances(): Unit
    open fun zoomCamera(): Unit
    open fun panCamera(): Unit
    open fun rotateCamera(): Unit
    open fun handleResize(): Unit
    open fun handleEvent(event: Any): Unit
}