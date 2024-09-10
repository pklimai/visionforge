package space.kscience.visionforge.meta

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import space.kscience.dataforge.context.Global
import space.kscience.dataforge.context.request
import space.kscience.dataforge.meta.*
import space.kscience.visionforge.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

internal class PropertyFlowTest {

    private val manager = Global.request(VisionManager)

    @Test
    fun testChildrenPropertyFlow() = runTest(timeout = 200.milliseconds) {
        val parent = MutableVisionGroup(manager){

            properties {
                "test" put 11
            }

            group("child") {
                properties {
                    "test" put 22
                }
            }

        }

        val child = parent.getVision("child") as MutableVisionGroup<*>

        val changesFlow = child.flowProperty("test", inherited = true).map {
            it.int!!
        }

//        child.inheritedEventFlow().filterIsInstance<VisionPropertyChangedEvent>().onEach { event ->
//            println(event)
//            delay(2)
//            println(child.readProperty("test", inherited = true))
//        }.launchIn(this)

        val collectedValues = ArrayList<Int>(5)

        val collectorJob = changesFlow.onEach {
            collectedValues.add(it)
        }.launchIn(this)


        delay(2)
        assertEquals(22, child.readProperty("test", true).int)

        child.properties.remove("test")
        delay(2)

        assertEquals(11, child.readProperty("test", true).int)
        parent.properties["test"] = 33
        delay(2)

        assertEquals(33, child.readProperty("test", true).int)

        collectorJob.cancel()
        assertEquals(listOf(22, 11, 33), collectedValues)
    }
}