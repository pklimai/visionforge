package space.kscience.visionforge.solid

import space.kscience.dataforge.context.Global
import space.kscience.dataforge.context.request
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.asValue
import space.kscience.dataforge.names.asName
import space.kscience.visionforge.VisionChange
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class VisionUpdateTest {
    val solidManager = Global.request(Solids)
    val visionManager = solidManager.visionManager

    @Test
    fun testVisionUpdate() {
        val targetVision = testSolids.solidGroup {
            box(200, 200, 200, name = "origin")
        }
        val dif = visionManager.VisionChange {
            solidGroup("top") {
                color(123)
                box(100, 100, 100)
            }
            propertyChanged("top".asName(), SolidMaterial.MATERIAL_COLOR_KEY, Meta("red".asValue()))
            propertyChanged("origin".asName(), SolidMaterial.MATERIAL_COLOR_KEY, Meta("red".asValue()))
        }
        targetVision.update(dif)
        assertTrue { targetVision.items.getChild("top") is SolidGroup }
        assertEquals("red", (targetVision.items.getChild("origin") as Solid).color.string) // Should work
        assertEquals(
            "#00007b",
            (targetVision.items.getChild("top") as Solid).color.string
        ) // new item always takes precedence
    }

    @Test
    fun testVisionChangeSerialization() {
        val change = visionManager.VisionChange {
            solidGroup("top") {
                color(123)
                box(100, 100, 100)
            }
            propertyChanged("top".asName(), SolidMaterial.MATERIAL_COLOR_KEY, Meta("red".asValue()))
            propertyChanged("origin".asName(), SolidMaterial.MATERIAL_COLOR_KEY, Meta("red".asValue()))
        }
        val serialized = visionManager.jsonFormat.encodeToString(VisionChange.serializer(), change)
        println(serialized)
        val reconstructed = visionManager.jsonFormat.decodeFromString(VisionChange.serializer(), serialized)
        assertEquals(change.properties, reconstructed.properties)
    }
}