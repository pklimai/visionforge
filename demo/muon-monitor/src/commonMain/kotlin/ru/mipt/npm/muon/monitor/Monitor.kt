package ru.mipt.npm.muon.monitor

import ru.mipt.npm.muon.monitor.Monitor.PIXEL_XY_SIZE
import ru.mipt.npm.muon.monitor.Monitor.PIXEL_Z_SIZE
import space.kscience.kmath.geometry.euclidean3d.Float32Space3D
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D

/**
 * A single pixel
 */
class SC1(
    val name: String,
    val center: Float32Vector3D,
    val xSize: Float = PIXEL_XY_SIZE, val ySize: Float = PIXEL_XY_SIZE, val zSize: Float = PIXEL_Z_SIZE,
)

class SC16(
    val name: String,
    val center: Float32Vector3D,
) {

    /**
     * Build map for single SC16 detector
     */
    val pixels: Collection<SC1> by lazy {
        (0 until 16).map { index ->
            val x: Double
            val y: Double
            when (index) {
                7 -> {
                    x = 1.5 * Monitor.PIXEL_XY_SPACING
                    y = 1.5 * Monitor.PIXEL_XY_SPACING
                }

                4 -> {
                    x = 0.5 * Monitor.PIXEL_XY_SPACING
                    y = 1.5 * Monitor.PIXEL_XY_SPACING
                }

                6 -> {
                    x = 1.5 * Monitor.PIXEL_XY_SPACING
                    y = 0.5 * Monitor.PIXEL_XY_SPACING
                }

                5 -> {
                    x = 0.5 * Monitor.PIXEL_XY_SPACING
                    y = 0.5 * Monitor.PIXEL_XY_SPACING
                }

                3 -> {
                    x = -1.5 * Monitor.PIXEL_XY_SPACING
                    y = 1.5 * Monitor.PIXEL_XY_SPACING
                }

                0 -> {
                    x = -0.5 * Monitor.PIXEL_XY_SPACING
                    y = 1.5 * Monitor.PIXEL_XY_SPACING
                }

                2 -> {
                    x = -1.5 * Monitor.PIXEL_XY_SPACING
                    y = 0.5 * Monitor.PIXEL_XY_SPACING
                }

                1 -> {
                    x = -0.5 * Monitor.PIXEL_XY_SPACING
                    y = 0.5 * Monitor.PIXEL_XY_SPACING
                }

                11 -> {
                    x = -1.5 * Monitor.PIXEL_XY_SPACING
                    y = -1.5 * Monitor.PIXEL_XY_SPACING
                }

                8 -> {
                    x = -0.5 * Monitor.PIXEL_XY_SPACING
                    y = -1.5 * Monitor.PIXEL_XY_SPACING
                }

                10 -> {
                    x = -1.5 * Monitor.PIXEL_XY_SPACING
                    y = -0.5 * Monitor.PIXEL_XY_SPACING
                }

                9 -> {
                    x = -0.5 * Monitor.PIXEL_XY_SPACING
                    y = -0.5 * Monitor.PIXEL_XY_SPACING
                }

                15 -> {
                    x = 1.5 * Monitor.PIXEL_XY_SPACING
                    y = -1.5 * Monitor.PIXEL_XY_SPACING
                }

                12 -> {
                    x = 0.5 * Monitor.PIXEL_XY_SPACING
                    y = -1.5 * Monitor.PIXEL_XY_SPACING
                }

                14 -> {
                    x = 1.5 * Monitor.PIXEL_XY_SPACING
                    y = -0.5 * Monitor.PIXEL_XY_SPACING
                }

                13 -> {
                    x = 0.5 * Monitor.PIXEL_XY_SPACING
                    y = -0.5 * Monitor.PIXEL_XY_SPACING
                }

                else -> throw Error()
            }
            val offset = Float32Vector3D(-y, x, 0)//rotateDetector(Point3D(x, y, 0.0));
            val pixelName = "${name}_${index}"
            SC1(pixelName, with(Float32Space3D) { offset + center })
        }
    }
}

//class Layer(val name: String, val z: Double) {
//    val detectors: Collection<SC16> by lazy {
//
//    }
//}


expect fun readResource(path: String): String

internal expect fun readMonitorConfig(): String

/**
 * General geometry definitions
 * Created by darksnake on 09-May-16.
 */
object Monitor {

    const val GEOMETRY_TOLERANCE = 0.01
    const val PIXEL_XY_SIZE = 122.0f
    const val PIXEL_XY_SPACING = 123.2f
    const val PIXEL_Z_SIZE = 30.0f
    const val CENTRAL_LAYER_Z = 0.0f
    const val UPPER_LAYER_Z = -166.0f
    const val LOWER_LAYER_Z = 180.0f

    /**
     * Build map for the whole monitor
     */
    val detectors: Collection<SC16> by lazy {
        readMonitorConfig()
            .lineSequence()
            .mapNotNull { line ->
                if (line.startsWith(" ")) {
                    val split = line.trim().split("\\s+".toRegex())
                    val detectorName = split[1]
                    val x = split[4].toDouble() - 500
                    val y = split[5].toDouble() - 500
                    val z = 180 - split[6].toDouble()
                    SC16(detectorName, Float32Vector3D(x, y, z))
                } else {
                    null
                }
            }.toList()
    }

    val pixels: Collection<SC1> get() = detectors.flatMap { it.pixels }

}