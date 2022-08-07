package space.kscience.visionforge.solid

import space.kscience.dataforge.names.Name
import space.kscience.visionforge.Colors
import space.kscience.visionforge.get
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 * Create and attach new proxied group
 */
fun SolidGroup.refGroup(
    name: String,
    templateName: Name = Name.parse(name),
    block: SolidGroup.() -> Unit
): SolidReference {
    val group = SolidGroup().apply(block)
    return newRef(name, group, prototypeName = templateName)
}


class SerializationTest {
    @Test
    fun testCubeSerialization() {
        val cube = Box(100f, 100f, 100f).apply {
            color.set(222)
            x = 100
            z = -100
        }
        val string = Solids.encodeToString(cube)
        println(string)
        val newCube = Solids.decodeFromString(string)
        assertEquals(cube.properties.raw, newCube.properties.raw)
    }

    @Test
    fun testProxySerialization() {
        val cube = Box(100f, 100f, 100f).apply {
            color.set(222)
            x = 100
            z = -100
        }
        val group = SolidGroup {
            newRef("cube", cube)
            refGroup("pg", Name.parse("pg.content")) {
                sphere(50) {
                    x = -100
                }
            }
        }
        val string = Solids.encodeToString(group)
        println(string)
        val reconstructed = Solids.decodeFromString(string) as SolidGroup
        assertEquals(group.children["cube"]?.properties?.raw, reconstructed.children["cube"]?.properties?.raw)
    }

    @Test
    fun lightSerialization(){
        val group = SolidGroup {
            ambientLight {
                color.set(Colors.white)
                intensity = 100.0
            }
        }
        val serialized = Solids.encodeToString(group)

        val reconstructed = Solids.decodeFromString(serialized) as SolidGroup
        assertEquals(100.0, (reconstructed.children["@ambientLight"] as AmbientLightSource).intensity.toDouble())
    }

}