package hep.dataforge.vis.spatial.three

import hep.dataforge.meta.node
import hep.dataforge.vis.spatial.PolyLine
import hep.dataforge.vis.spatial.color
import hep.dataforge.vis.spatial.meshline.MeshLine
import hep.dataforge.vis.spatial.meshline.MeshLineMaterial
import hep.dataforge.vis.spatial.three.ThreeMaterials.DEFAULT_LINE_COLOR
import info.laht.threekt.core.Geometry
import info.laht.threekt.core.Object3D
import info.laht.threekt.math.Color
import info.laht.threekt.objects.LineSegments
import info.laht.threekt.objects.Mesh
import kotlin.reflect.KClass

object ThreeLineFactoryOLD : ThreeFactory<PolyLine> {
    override val type: KClass<PolyLine> get() = PolyLine::class

    override fun invoke(obj: PolyLine): Object3D {
        val geometry = Geometry().apply {
            vertices = obj.points.toTypedArray()
        }

        val material =
            ThreeMaterials.getLineMaterial(obj.getProperty(MeshThreeFactory.EDGES_MATERIAL_KEY).node)

        material.linewidth = obj.thickness.toDouble()
        material.color = obj.color?.let { Color(it) }?: DEFAULT_LINE_COLOR

        return LineSegments(geometry, material).apply {
            updatePosition(obj)
            //layers.enable(obj.layer)
            //add listener to object properties
            obj.onPropertyChange(this) { propertyName, _, _ ->
                updateProperty(obj, propertyName)
            }
        }
    }

}

object ThreeLineFactory : ThreeFactory<PolyLine> {
    override val type: KClass<PolyLine> get() = PolyLine::class

    override fun invoke(obj: PolyLine): Object3D {
        val geometry = Geometry().apply {
            vertices = obj.points.toTypedArray()
        }

        val line = MeshLine().apply {
            setGeometry(geometry)
        }

        val material = MeshLineMaterial().apply {
            lineWidth = 0.02f //obj.thickness.toFloat()
            color = obj.color?.let { Color(it) } ?: DEFAULT_LINE_COLOR
            sizeAttenuation = 0
            near = 0.1f
            far = 10000f
        }

        return Mesh(line.geometry, material).apply {
            updatePosition(obj)
            obj.onPropertyChange(this) { propertyName, _, _ ->
                updateProperty(obj, propertyName)
            }
        }
    }

}
