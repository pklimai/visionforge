package space.kscience.visionforge.solid

import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MetaProvider
import space.kscience.dataforge.meta.float
import space.kscience.dataforge.meta.get
import space.kscience.kmath.geometry.euclidean2d.Float32Space2D
import space.kscience.kmath.geometry.euclidean2d.Float32Vector2D
import space.kscience.kmath.geometry.euclidean3d.Float32Space3D
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D
import space.kscience.visionforge.solid.Solid.Companion.X_KEY
import space.kscience.visionforge.solid.Solid.Companion.Y_KEY
import space.kscience.visionforge.solid.Solid.Companion.Z_KEY
import kotlin.math.PI

public typealias FloatVector2D = @Serializable(with = Float32Space2D.VectorSerializer::class) Float32Vector2D
public typealias FloatVector3D = @Serializable(with = Float32Space3D.VectorSerializer::class) Float32Vector3D

public const val PI2: Float = 2 * PI.toFloat()

public fun FloatVector2D.toMeta(): Meta = Meta {
    X_KEY put x
    Y_KEY put y
}

internal fun Meta.toVector2D(): FloatVector2D =
    Float32Vector2D(this["x"].float ?: 0f, this["y"].float ?: 0f)

//@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
//@Serializable(Point3DSerializer::class)
//public interface MutablePoint3D : Vector3D {
//    override var x: Float
//    override var y: Float
//    override var z: Float
//}
//
//
//public fun MutablePoint3D.normalizeInPlace() {
//    val norm = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
//    x /= norm
//    y /= norm
//    z /= norm
//}

internal fun MetaProvider.point3D(default: Float = 0f) = Float32Space3D.vector(
    get(X_KEY).float ?: default,
    get(Y_KEY).float ?: default,
    get(Z_KEY).float ?: default
)


public fun FloatVector3D.toMeta(): Meta = Meta {
    X_KEY put x
    Y_KEY put y
    Z_KEY put z
}


internal fun Meta.toVector3D(default: Float = 0f) = Float32Vector3D(
    this[X_KEY].float ?: default,
    this[Y_KEY].float ?: default,
    this[Z_KEY].float ?: default
)

//internal fun Solid.updatePosition(meta: Meta?) {
//    meta?.get(Solid.POSITION_KEY)?.toVector()?.let { position = it }
//    meta?.get(Solid.ROTATION_KEY)?.toVector()?.let { rotation = it }
//    meta?.get(Solid.SCALE_KEY)?.toVector(1f)?.let { scale = it }
//}