package space.kscience.visionforge.solid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.update
import space.kscience.kmath.geometry.component1
import space.kscience.kmath.geometry.component2
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D
import space.kscience.visionforge.MutableVisionContainer
import space.kscience.visionforge.VisionBuilder
import space.kscience.visionforge.properties


/**
 * An extruded shape with the same number of points on each layer.
 */
@Serializable
@SerialName("solid.extrude")
public class Extruded(
    public val shape: Shape2D,
    public val layers: List<Layer>,
) : SolidBase<Extruded>(), GeometrySolid {

    /**
     * A layer for extruded shape
     */
    @Serializable
    public data class Layer(var x: Float, var y: Float, var z: Float, var scale: Float)

    init {
        require(shape.size > 2) { "Extruded shape requires more than 2 points per layer" }
    }

    override fun <T : Any> toGeometry(geometryBuilder: GeometryBuilder<T>) {

        /**
         * Expand the shape for specific layers
         */
        val layers: List<List<FloatVector3D>> = layers.map { layer ->
            shape.map { (x, y) ->
                val newX = layer.x + x * layer.scale
                val newY = layer.y + y * layer.scale
                Float32Vector3D(newX, newY, layer.z)
            }
        }

        if (layers.size < 2) error("Extruded shape requires more than one layer")

        var lowerLayer = layers.first()
        var upperLayer: List<FloatVector3D>

        geometryBuilder.cap(layers.first().reversed())

        for (i in (1 until layers.size)) {
            upperLayer = layers[i]
            for (j in (0 until shape.size - 1)) {
                //counterclockwise
                geometryBuilder.face4(
                    lowerLayer[j],
                    lowerLayer[j + 1],
                    upperLayer[j + 1],
                    upperLayer[j]
                )
            }

            // final face
            geometryBuilder.face4(
                lowerLayer[shape.size - 1],
                lowerLayer[0],
                upperLayer[0],
                upperLayer[shape.size - 1]
            )
            lowerLayer = upperLayer
        }

        geometryBuilder.cap(layers.last())
    }

    public class Builder(
        public var shape: List<FloatVector2D> = emptyList(),
        public var layers: MutableList<Layer> = ArrayList(),
        public val properties: MutableMeta = MutableMeta(),
    ) {
        @VisionBuilder
        public fun shape(block: Shape2DBuilder.() -> Unit) {
            this.shape = Shape2DBuilder().apply(block).build()
        }

        @VisionBuilder
        public fun layer(z: Number, x: Number = 0.0, y: Number = 0.0, scale: Number = 1.0) {
            layers.add(Layer(x.toFloat(), y.toFloat(), z.toFloat(), scale.toFloat()))
        }

        internal fun build(): Extruded = Extruded(shape, layers).apply {
            properties {
                update(this@Builder.properties)
            }
        }
    }

    public companion object {
        public const val TYPE: String = "solid.extruded"
    }
}

@VisionBuilder
public fun MutableVisionContainer<Solid>.extruded(
    name: String? = null,
    action: Extruded.Builder.() -> Unit = {},
): Extruded = Extruded.Builder().apply(action).build().also {
    setVision(SolidGroup.inferNameFor(name, it), it)
}