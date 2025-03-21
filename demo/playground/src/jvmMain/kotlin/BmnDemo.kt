package space.kscience.visionforge.examples

import ru.mipt.npm.root.BMN
import ru.mipt.npm.root.DGeoManager
import ru.mipt.npm.root.rootGeo
import ru.mipt.npm.root.serialization.TGeoManager
import ru.mipt.npm.root.toVector
import space.kscience.dataforge.meta.*
import space.kscience.visionforge.Colors
import space.kscience.visionforge.html.ResourceLocation
import space.kscience.visionforge.html.meta
import space.kscience.visionforge.solid.*
import java.util.zip.ZipInputStream
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText


private fun Meta.countTypes(): Sequence<String> = sequence {
    if (!isLeaf) {
        get("_typename")?.value?.let { yield(it.string) }
        items.forEach { yieldAll(it.value.countTypes()) }
    }
}

fun main() {
    Path("data").createDirectories()

    val string = ZipInputStream(TGeoManager::class.java.getResourceAsStream("/root/geometry_run_7-2076.zip")!!).use {
        it.nextEntry
        it.readAllBytes().decodeToString()
    }

    val geo = DGeoManager.parse(string)


    val sizes = geo.meta.countTypes().groupBy { it }.mapValues { it.value.size }
    sizes.forEach {
        println(it)
    }

    val events = BMN.readEventJson(
        TGeoManager::class.java.getResourceAsStream("/root/event_0.json")!!.bufferedReader().readText()
    )

    makeVisionFile(path = Path("data/output.html"), resourceLocation = ResourceLocation.EMBED) {
        vision("canvas") {
            requirePlugin(Solids)
            meta {
                "layers" put ListValue(0, 1, 2, 3, 4, 5, 6)
            }
            solid {
                ambientLight {
                    color(Colors.white)
                }

                rootGeo(geo, "BM@N", ignoreRootColors = true).also {
                    Path("data/BM@N.vf.json").writeText(Solids.encodeToString(it))
                }

                solidGroup("cbmStsTracks") {
                    events.cbmTracks.forEach { track ->
                        polyline(
                            track.fParamFirst.toVector(),
                            track.fParamLast.toVector()
                        ) {
                            thickness = 2.0
                            color(Colors.blue)
                        }
                    }
                }

                solidGroup("bmnGlobalTracks") {
                    events.bmnGlobalTracks.forEach { track ->
                        polyline(
                            track.fParamFirst.toVector(),
                            track.fParamLast.toVector()
                        ) {
                            thickness = 2.0
                            color(Colors.red)
                        }
                    }
                }
            }
        }
    }
}