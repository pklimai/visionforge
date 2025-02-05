package space.kscience.visionforge.solid

import kotlinx.serialization.json.encodeToJsonElement
import space.kscience.dataforge.meta.string
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.names.get
import space.kscience.dataforge.names.parseAsName
import space.kscience.visionforge.style
import space.kscience.visionforge.useStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DFExperimental
class SolidReferenceTest {
    val groupWithReference = testSolids.solidGroup {
        val theStyle by style {
            SolidMaterial.MATERIAL_COLOR_KEY put "red"
        }
        newRef("test", Box(100f, 100f, 100f).apply {
            color("blue")
            useStyle(theStyle)
        })
    }

    @Test
    fun testReferenceProperty() {
        assertEquals("blue", (groupWithReference.get("test") as Solid).color.string)
    }

    @Test
    fun testReferenceSerialization() {
        val serialized = Solids.jsonForSolids.encodeToJsonElement(groupWithReference)
        val deserialized = Solids.jsonForSolids.decodeFromJsonElement(SolidGroup.serializer(), serialized)
        assertEquals(groupWithReference.visions["test"]?.color?.string, deserialized.visions["test"]?.color?.string)
        assertEquals("blue", (deserialized["test"] as Solid).color.string)
    }

    @Test
    fun refRef() {
        val group = testSolids.solidGroup {
            prototypes {
                solidGroup("group") {
                    box(100, 100, 100, "refGroupChild")
                }
                ref("group.refGroupChild".parseAsName(), "refRef")
            }
            ref("refRef".parseAsName(), "refRefRef")
            ref("group".parseAsName(), "groupRef")
        }

        val boxRef = group["refRefRef"]!!

        boxRef.color("red")

        assertTrue { boxRef.prototype is SolidReference }

        assertTrue { boxRef.prototype.prototype is Box }

        assertEquals("red", boxRef.properties[SolidMaterial.MATERIAL_COLOR_KEY].string)
        assertEquals(null, boxRef.prototype.properties[SolidMaterial.MATERIAL_COLOR_KEY])
        assertEquals(null, boxRef.prototype.prototype.properties[SolidMaterial.MATERIAL_COLOR_KEY])

        val groupRef = group["groupRef"] as SolidContainer
        val groupChildRef = groupRef.getVision("refGroupChild")

        assertTrue(groupChildRef?.prototype is Box)

        groupChildRef.color("blue")

        assertEquals("blue", groupChildRef.properties[SolidMaterial.MATERIAL_COLOR_KEY].string)
        assertEquals(null, boxRef.prototype.properties[SolidMaterial.MATERIAL_COLOR_KEY])
        assertEquals(null, boxRef.prototype.prototype.properties[SolidMaterial.MATERIAL_COLOR_KEY])
    }
}