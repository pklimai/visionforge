@file:OptIn(DFExperimental::class)

package space.kscience.plotly

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.node
import space.kscience.dataforge.misc.DFBuilder
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.NameToken
import space.kscience.plotly.models.Layout
import space.kscience.plotly.models.Trace
import space.kscience.visionforge.*

@Serializable
public class VisionOfTrace(
    traceMeta: MutableMeta,
) : AbstractVision(traceMeta)


/**
 * The main plot class.
 */
@DFBuilder
@Serializable
public class Plot : AbstractVision(), VisionGroup {

    private val traces = mutableListOf<VisionOfTrace>()

    @Transient
    private val traceFlow = MutableSharedFlow<Name>()

    override val children: VisionChildren = object : VisionChildren {
        override val parent: Vision get() = this@Plot

        override val keys: Collection<NameToken> get() = traces.indices.map { NameToken("trace", it.toString()) }

        override val changes: Flow<Name> get() = traceFlow

        override fun get(token: NameToken): VisionOfTrace? {
            return if (token.body == "trace") {
                val index = token.index?.toIntOrNull() ?: return null
                traces.getOrNull(index)
            } else null
        }
    }

    /**
     * Ordered list ot traces in the plot
     */
    public val data: List<Trace> get() = traces.map { Trace.write(properties.root()) }

    /**
     * Layout specification for th plot
     */
    public val layout: Layout by properties.root().scheme(Layout)

    public fun addTrace(trace: Trace) {
        traces.add(VisionOfTrace((trace)))
    }

    /**
     * Append all traces from [traces] to the plot
     */
    public fun traces(traces: Collection<Trace>) {
        traces.forEach { addTrace(it) }
    }

    /**
     * Append all [traces]
     */
    public fun traces(vararg traces: Trace) {
        traces.forEach { addTrace(it) }
    }

    /**
     * Remove a trace with a given [index] from the plot
     */
    @UnstablePlotlyAPI
    internal fun removeTrace(index: Int) {
        traces.removeAt(index)
    }

    override val descriptor: MetaDescriptor get() = Companion.descriptor

    public companion object {
        public val descriptor: MetaDescriptor = MetaDescriptor {
            node(Plot::data.name, Trace) {
                multiple = true
            }

            node(Plot::layout.name, Layout)
        }
    }
}

public fun Plot(meta: Meta): Plot = Plot().apply {
    meta["layout"]?.let { layoutMeta -> layout { update(layoutMeta) } }
    meta.getIndexed("data").forEach { (_, data) ->
        addTrace(Trace.read(data))
    }
}

/**
 * Add plot data change listener to each trace
 */
public fun Plot.onDataChange(owner: Any?, callback: (index: Int, trace: Trace, propertyName: Name) -> Unit) {
    data.forEachIndexed { index, trace ->
        trace.meta.onChange(owner) { name ->
            callback(index, trace, name)
        }
    }
}


/**
 * Remove change listeners with given [owner] from all traces
 */
public fun Plot.removeChangeListener(owner: Any?) {
    data.forEach { trace ->
        trace.meta.removeListener(owner)
    }
}

internal fun Plot.toJson(): JsonObject = buildJsonObject {
    put("layout", layout.meta.toJson())
    put("data", buildJsonArray {
        data.forEach { traceData ->
            add(traceData.meta.toJson())
        }
    })
}

/**
 * Convert a plot to Json representation specified by Plotly `newPlot` command.
 */
public fun Plot.toJsonString(): String = toJson().toString()

/**
 * Configure the layout
 */
public inline fun Plot.layout(block: Layout.() -> Unit) {
    layout.invoke(block)
}

/**
 * Add a generic trace
 */
public inline fun Plot.trace(block: Trace.() -> Unit): Trace {
    val trace = Trace(block)
    traces(trace)
    return trace
}