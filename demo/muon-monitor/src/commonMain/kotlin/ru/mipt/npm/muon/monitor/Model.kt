package ru.mipt.npm.muon.monitor

import ru.mipt.npm.muon.monitor.Monitor.CENTRAL_LAYER_Z
import ru.mipt.npm.muon.monitor.Monitor.LOWER_LAYER_Z
import ru.mipt.npm.muon.monitor.Monitor.UPPER_LAYER_Z
import space.kscience.kmath.geometry.euclidean3d.Float32Vector3D
import space.kscience.visionforge.MutableVisionContainer
import space.kscience.visionforge.VisionManager
import space.kscience.visionforge.setAsRoot
import space.kscience.visionforge.solid.*
import kotlin.math.PI

class Model(val manager: VisionManager) {
    private val map = HashMap<String, SolidGroup>()
    private val events = HashSet<Event>()

    private fun MutableVisionContainer<Solid>.pixel(pixel: SC1) {
        val group = solidGroup(pixel.name) {
            position = Float32Vector3D(pixel.center.x, pixel.center.y, pixel.center.z)
            box(pixel.xSize, pixel.ySize, pixel.zSize)
            label(pixel.name) {
                z = -Monitor.PIXEL_Z_SIZE / 2 - 5
                rotationY = PI
            }
        }
        map[pixel.name] = group
    }

    private fun SolidGroup.detector(detector: SC16) {
        solidGroup(detector.name) {
            detector.pixels.forEach {
                pixel(it)
            }
        }
    }

    val tracks: SolidGroup = SolidGroup()

    val root: SolidGroup = SolidGroup().apply {
        setAsRoot(this@Model.manager)
        material {
            color("darkgreen")
        }
        rotationX = PI / 2
        solidGroup("bottom") {
            Monitor.detectors.filter { it.center.z == LOWER_LAYER_Z }.forEach {
                detector(it)
            }
        }

        solidGroup("middle") {
            Monitor.detectors.filter { it.center.z == CENTRAL_LAYER_Z }.forEach {
                detector(it)
            }
        }

        solidGroup("top") {
            Monitor.detectors.filter { it.center.z == UPPER_LAYER_Z }.forEach {
                detector(it)
            }
        }

        set("tracks", tracks)
    }

    private fun highlight(pixel: String) {
        println("highlight $pixel")
        map[pixel]?.color("blue")
    }

    fun reset() {
        map.values.forEach {
            it.properties[SolidMaterial.MATERIAL_COLOR_KEY] = null
        }
        val tracksToRemove = tracks.visions.keys.toList()
        tracksToRemove.forEach {
            tracks.setVision(it, null)
        }
    }

    fun displayEvent(event: Event) {
        println("Received event: $event")
        events.add(event)
        event.hits.forEach {
            highlight(it)
        }
        event.track?.let {
            tracks.polyline(*it.toTypedArray(), name = "track[${event.id}]") {
                color("red")
            }
        }
    }

    fun encodeToString(): String = manager.encodeToString(this.root)
}