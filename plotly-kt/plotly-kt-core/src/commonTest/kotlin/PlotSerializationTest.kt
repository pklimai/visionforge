package space.kscience.plotly

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import space.kscience.dataforge.meta.ListValue
import space.kscience.dataforge.meta.ObservableMutableMeta
import space.kscience.plotly.models.ShapeType
import space.kscience.plotly.models.TraceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PlotSerializationTest {
    @Test
    fun deserialization() {
        val meta = ObservableMutableMeta {
            "data" put {
                "x" put ListValue(1, 2, 3)
                "y" put ListValue(5, 6, 7)
                "type" put "scatter"
            }
        }

        val plot = Plot(meta)
        assertEquals(1, plot.data.size)
        assertEquals(TraceType.scatter, plot.data[0].type)
        assertEquals(1.0, plot.data[0].x.doubles[0])
    }

    @Test
    fun shapeSerialization() {
        val plot = Plotly.plot {
            shape {
                type = ShapeType.rect
            }
        }

        val json = plot.toJson()
        println(json.toString())
        val shapes = json["layout"]?.jsonObject?.get("shapes")
        assertTrue { shapes is JsonArray }
    }
}