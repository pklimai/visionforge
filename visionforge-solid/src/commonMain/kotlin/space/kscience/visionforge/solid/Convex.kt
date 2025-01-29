package space.kscience.visionforge.solid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.names.NameToken
import space.kscience.kmath.geometry.euclidean3d.Float32Space3D
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D
import space.kscience.visionforge.MutableVisionContainer

/**
 * A convex hull shape
 */
@Serializable
@SerialName("solid.convex")
public class Convex(
    public val points: List<@Serializable(Float32Space3D.VectorSerializer::class) Float32Vector3D>
) : SolidBase<Convex>()

public inline fun MutableVisionContainer<Solid>.convex(
    name: String? = null,
    action: ConvexBuilder.() -> Unit = {},
): Convex =    ConvexBuilder().apply(action).build().also {
    setVision(name?.let(NameToken::parse) ?: SolidGroup.staticNameFor(it), it)
}

public class ConvexBuilder {
    private val points = ArrayList<Float32Vector3D>()

    public fun point(x: Number, y: Number, z: Number) {
        points.add(Float32Vector3D(x, y, z))
    }

    public fun build(): Convex {
        return Convex(points)
    }
}