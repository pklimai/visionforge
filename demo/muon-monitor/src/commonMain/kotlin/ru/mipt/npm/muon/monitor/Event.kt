@file:UseSerializers(Float32Space3D.VectorSerializer::class)
package ru.mipt.npm.muon.monitor

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import space.kscience.kmath.geometry.euclidean3d.Float32Space3D
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D


typealias Track = List<Float32Vector3D>

/**
 *
 */
@Serializable
data class Event(val id: Int, val track: Track?, val hits: Collection<String>) {
//    /**
//     * The unique identity for given set of hits. One identity could correspond to different tracks
//     */
//    val id get() = hits.sorted().joinToString(separator = ", ", prefix = "[", postfix = "]")
}