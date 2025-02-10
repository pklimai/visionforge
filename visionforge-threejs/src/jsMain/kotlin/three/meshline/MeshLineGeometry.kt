@file:JsModule("meshline")


package three.meshline

import three.core.BufferGeometry
import three.materials.ShaderMaterial
import three.math.Color
import three.math.Vector2
import three.math.Vector3
import three.textures.Texture

/*
 * https://github.com/spite/THREE.MeshLine
 */

public external class MeshLineGeometry : BufferGeometry {
//    public fun setGeometry(geometry: BufferGeometry)
    public fun setPoints(points: Array<Vector3>)
}

public external class MeshLineMaterial : ShaderMaterial {
    public var lineWidth: Float
    public var color: Color

    public var map: Texture?
    public var useMap: Float
    public var alphaMap: Texture?
    public var useAlphaMap: Float
    public var gradient: Array<Color>

    public var repeat: Vector2 // - THREE.Vector2 to define the texture tiling (applies to map and alphaMap - MIGHT CHANGE IN THE FUTURE)
    public var dashArray: Float //- the length and space between dashes. (0 - no dash)
    public var dashOffset: dynamic // - defines the location where the dash will begin. Ideal to animate the line.
    public var dashRatio: dynamic // - defines the ratio between that is visible or not (0 - more visible, 1 - more invisible).
    public var resolution: Vector2 // - THREE.Vector2 specifying the canvas size (REQUIRED)
    public var sizeAttenuation: Int // - makes the line width constant regardless distance (1 unit is 1px on screen) (0 - attenuate, 1 - don't attenuate)
}