package space.kscience.visionforge.solid

import kotlinx.serialization.Serializable
import space.kscience.kmath.geometry.euclidean2d.Float32Vector2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public typealias Shape2D = List<FloatVector2D>

/**
 * A builder for 2D shapes
 */
@Serializable
public class Shape2DBuilder(private val points: ArrayList<FloatVector2D> = ArrayList()) {

    public fun point(x: Number, y: Number) {
        points.add(Float32Vector2D(x, y))
    }

    public fun build(): Shape2D = points
}

public fun Shape2DBuilder.polygon(vertices: Int, radius: Number) {
    require(vertices > 2) { "Polygon must have more than 2 vertices" }
    val angle = 2 * PI / vertices
    for (i in 0 until vertices) {
        point(radius.toDouble() * cos(angle * i), radius.toDouble() * sin(angle * i))
    }
}