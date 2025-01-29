package space.kscience.visionforge.solid

import kotlinx.coroutines.test.runTest
import space.kscience.dataforge.context.Global
import space.kscience.dataforge.context.request
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.names.asName
import space.kscience.visionforge.VisionChange
import space.kscience.visionforge.getOrCreateChange
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class VisionUpdateTest {
    val solidManager = Global.request(Solids)
    val visionManager = solidManager.visionManager

    @Test
    fun testVisionUpdate() = runTest {
        val targetVision = testSolids.solidGroup {
            box(200, 200, 200, name = "origin")
        }
        val dif = visionManager.VisionChange {
            solidGroup("top") {
                color(123)
                box(100, 100, 100)
            }

            getOrCreateChange("top".asName()).propertyChanged(
                SolidMaterial.MATERIAL_COLOR_KEY,
                Meta("red")
            )
            getOrCreateChange("origin".asName()).propertyChanged(
                SolidMaterial.MATERIAL_COLOR_KEY,
                Meta("red")
            )
        }
        targetVision.receiveEvent(dif)
        assertTrue { targetVision["top"] is SolidGroup }
        assertEquals("red", (targetVision["origin"] as Solid).color.string) // Should work
        assertEquals(
            "#00007b",
            (targetVision["top"] as Solid).color.string
        ) // new item always takes precedence
    }

    @Test
    fun testVisionChangeSerialization() {
        val change = visionManager.VisionChange {
            solidGroup("top") {
                color(123)
                box(100, 100, 100)
            }
            getOrCreateChange("top".asName()).propertyChanged(
                SolidMaterial.MATERIAL_COLOR_KEY,
                Meta("red")
            )
            getOrCreateChange("origin".asName()).propertyChanged(
                SolidMaterial.MATERIAL_COLOR_KEY,
                Meta("red")
            )
        }
        val serialized = visionManager.jsonFormat.encodeToString(VisionChange.serializer(), change)
        println(serialized)
        val reconstructed = visionManager.jsonFormat.decodeFromString(VisionChange.serializer(), serialized)
        assertEquals(change.properties, reconstructed.properties)
    }
}