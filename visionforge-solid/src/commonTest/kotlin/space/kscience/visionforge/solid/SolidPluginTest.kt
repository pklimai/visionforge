package space.kscience.visionforge.solid

import space.kscience.dataforge.context.Global
import space.kscience.dataforge.context.request
import space.kscience.dataforge.misc.DFExperimental
import kotlin.test.Test
import kotlin.test.assertEquals

internal val testSolids = Global.request(Solids)
internal val testVisionManager = testSolids.visionManager


class SolidPluginTest {
    val vision = testSolids.solidGroup {
        box(100, 100, 100, name = "aBox")

        sphere(100, name = "aSphere") {
            z = 200
        }
    }

    @DFExperimental
    @Test
    fun testPluginConverter() {
        val meta = testVisionManager.encodeToMeta(vision)

        val reconstructed = testVisionManager.decodeFromMeta(meta) as SolidGroup

        assertEquals(
            testVisionManager.encodeToJsonElement(vision["aBox"]!!),
            testVisionManager.encodeToJsonElement(reconstructed["aBox"]!!)
        )
    }
}