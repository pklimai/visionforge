package space.kscience.visionforge.gdml

import org.junit.jupiter.api.fail
import space.kscience.dataforge.context.Global
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.plus
import space.kscience.gdml.Gdml
import space.kscience.gdml.decodeFromStream
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionGroup
import space.kscience.visionforge.setAsRoot
import space.kscience.visionforge.visionManager
import kotlin.test.Test

class TestConsistency {

    private fun failOnOrphan(vision: Vision, prefix: Name = Name.EMPTY) {
        if(vision.parent == null) fail { "Parent is not defined for $vision with name '$prefix'" }
        if(vision is VisionGroup<*>){
            vision.visions.forEach { (token, child)->
                failOnOrphan(child, prefix + token)
            }
        }
    }

    @Test
    public fun noOrphans() {
        val stream = javaClass.getResourceAsStream("/gdml/cubes.gdml")!!
        val gdml = Gdml.decodeFromStream(stream)
        val vision = gdml.toVision()
        vision.setAsRoot(Global.visionManager)
        failOnOrphan(vision)
    }
}