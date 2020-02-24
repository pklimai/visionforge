@file:JsModule("three.meshline")
@file:JsNonModule

package hep.dataforge.vis.spatial.meshline

import info.laht.threekt.core.Geometry
import info.laht.threekt.materials.Material
import info.laht.threekt.math.Color

external class MeshLine {
    fun setGeometry(geometry: Geometry)
    var geometry: Geometry
}

external class MeshLineMaterial: Material {
    var lineWidth: Float
    var color: Color
    var sizeAttenuation: Int
    var near: Float
    var far: Float
}
