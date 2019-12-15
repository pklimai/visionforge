package hep.dataforge.vis.spatial.fx

import hep.dataforge.context.*
import hep.dataforge.meta.Meta
import hep.dataforge.meta.get
import hep.dataforge.provider.Type
import hep.dataforge.vis.spatial.*
import hep.dataforge.vis.spatial.fx.FX3DFactory.Companion.TYPE
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.shape.Shape3D
import javafx.scene.transform.Rotate
import org.fxyz3d.shapes.composites.PolyLine3D
import org.fxyz3d.shapes.primitives.CuboidMesh
import org.fxyz3d.shapes.primitives.SpheroidMesh
import kotlin.math.PI
import kotlin.reflect.KClass

class FX3DPlugin : AbstractPlugin() {
    override val tag: PluginTag get() = Companion.tag

    private val objectFactories = HashMap<KClass<out VisualObject3D>, FX3DFactory<*>>()
    private val compositeFactory = FXCompositeFactory(this)
    private val proxyFactory = FXProxyFactory(this)

    init {
        //Add specialized factories here
        objectFactories[Convex::class] = FXConvexFactory
    }


    @Suppress("UNCHECKED_CAST")
    private fun findObjectFactory(type: KClass<out VisualObject3D>): FX3DFactory<VisualObject3D>? {
        return (objectFactories[type] ?: context.content<FX3DFactory<*>>(TYPE).values.find { it.type == type })
                as FX3DFactory<VisualObject3D>?
    }

    fun buildNode(obj: VisualObject3D): Node {
        val binding = VisualObjectFXBinding(obj)
        return when (obj) {
            is Proxy -> proxyFactory(obj, binding)
            is VisualGroup3D -> {
                Group(obj.children.mapNotNull { (token, obj) ->
                    (obj as? VisualObject3D)?.let {
                        buildNode(it).apply {
                            properties["name"] = token.toString()
                        }
                    }
                })
            }
            is Composite -> compositeFactory(obj, binding)
            is Box -> CuboidMesh(obj.xSize.toDouble(), obj.ySize.toDouble(), obj.zSize.toDouble())
            is Sphere -> if (obj.phi == PI2 && obj.theta == PI.toFloat()) {
                //use sphere for orb
                SpheroidMesh(obj.detail ?: 16, obj.radius.toDouble(), obj.radius.toDouble())
            } else {
                FXShapeFactory(obj, binding)
            }
            is PolyLine -> PolyLine3D(
                obj.points.map { it.point },
                obj.thickness.toFloat(),
                obj.material?.get("color")?.color()
            )
            else -> {
                //find specialized factory for this type if it is present
                val factory: FX3DFactory<VisualObject3D>? = findObjectFactory(obj::class)
                when {
                    factory != null -> factory(obj, binding)
                    obj is Shape -> FXShapeFactory(obj, binding)
                    else -> error("Renderer for ${obj::class} not found")
                }
            }
        }.apply {
            translateXProperty().bind(binding[VisualObject3D.xPos].float(obj.x.toFloat()))
            translateYProperty().bind(binding[VisualObject3D.yPos].float(obj.y.toFloat()))
            translateZProperty().bind(binding[VisualObject3D.zPos].float(obj.z.toFloat()))
            scaleXProperty().bind(binding[VisualObject3D.xScale].float(obj.scaleX.toFloat()))
            scaleYProperty().bind(binding[VisualObject3D.yScale].float(obj.scaleY.toFloat()))
            scaleZProperty().bind(binding[VisualObject3D.zScale].float(obj.scaleZ.toFloat()))

            val rotateX = Rotate(0.0, Rotate.X_AXIS).apply {
                angleProperty().bind(binding[VisualObject3D.xRotation].float(obj.rotationX.toFloat()))
            }

            val rotateY = Rotate(0.0, Rotate.Y_AXIS).apply {
                angleProperty().bind(binding[VisualObject3D.yRotation].float(obj.rotationY.toFloat()))
            }

            val rotateZ = Rotate(0.0, Rotate.Z_AXIS).apply {
                angleProperty().bind(binding[VisualObject3D.zRotation].float(obj.rotationZ.toFloat()))
            }

            when (obj.rotationOrder) {
                RotationOrder.ZYX -> transforms.addAll(rotateZ, rotateY, rotateX)
                RotationOrder.XZY -> transforms.addAll(rotateY, rotateZ, rotateX)
                RotationOrder.YXZ -> transforms.addAll(rotateZ, rotateX, rotateY)
                RotationOrder.YZX -> transforms.addAll(rotateX, rotateZ, rotateY)
                RotationOrder.ZXY -> transforms.addAll(rotateY, rotateX, rotateZ)
                RotationOrder.XYZ -> transforms.addAll(rotateZ, rotateY, rotateX)
            }

            if (this is Shape3D) {
                materialProperty().bind(binding[Material3D.MATERIAL_KEY].transform {
                    it.material()
                })
            }
        }
    }

    companion object : PluginFactory<FX3DPlugin> {
        override val tag = PluginTag("visual.fx3D", PluginTag.DATAFORGE_GROUP)
        override val type = FX3DPlugin::class
        override fun invoke(meta: Meta, context: Context) = FX3DPlugin()
    }
}

/**
 * Builder and updater for three.js object
 */
@Type(TYPE)
interface FX3DFactory<in T : VisualObject3D> {

    val type: KClass<in T>

    operator fun invoke(obj: T, binding: VisualObjectFXBinding): Node

    companion object {
        const val TYPE = "fx3DFactory"
    }
}

