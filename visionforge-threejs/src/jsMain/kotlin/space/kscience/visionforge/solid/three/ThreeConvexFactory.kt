package space.kscience.visionforge.solid.three

import space.kscience.visionforge.solid.Convex
import three.external.geometries.ConvexGeometry

public object ThreeConvexFactory : ThreeMeshFactory<Convex>(Convex::class) {
    override suspend fun buildGeometry(obj: Convex): ConvexGeometry {
        val vectors = obj.points.map { it.toVector() }.toTypedArray()
        return ConvexGeometry(vectors)
    }
}