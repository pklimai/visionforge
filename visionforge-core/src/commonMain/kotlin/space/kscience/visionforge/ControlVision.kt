package space.kscience.visionforge

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.parseAsName

public abstract class VisionControlEvent : VisionEvent, MetaRepr {
    public abstract val meta: Meta

    override fun toMeta(): Meta = meta

    override fun toString(): String = toMeta().toString()
}

public interface ControlVision : MutableVision {
    /**
     * Fire a [VisionControlEvent] on this [ControlVision]
     */
    public suspend fun dispatchControlEvent(event: VisionControlEvent)

    override suspend fun receiveEvent(event: VisionEvent) {
        if (event is VisionControlEvent) {
            dispatchControlEvent(event)
        } else super.receiveEvent(event)
    }
}

public fun ControlVision.asyncControlEvent(
    event: VisionControlEvent,
    scope: CoroutineScope = manager?.context ?: error("Can't fire asynchronous event for an orphan vision. Provide a scope."),
) {
    scope.launch { dispatchControlEvent(event) }
}


@Serializable
public abstract class AbstractControlVision : AbstractVision(), ControlVision {

    override suspend fun dispatchControlEvent(event: VisionControlEvent) {
        emitEvent(event)
    }
}


/**
 * An event for submitting changes
 */
@Serializable
@SerialName("control.submit")
public class ControlSubmitEvent(override val meta: Meta) : VisionControlEvent() {
    public val payload: Meta get() = meta[::payload.name] ?: Meta.EMPTY

    public val name: Name? get() = meta["name"].string?.parseAsName()

    override fun toString(): String = meta.toString()
}

public fun ControlSubmitEvent(payload: Meta = Meta.EMPTY, name: Name? = null): ControlSubmitEvent = ControlSubmitEvent(
    Meta {
        ControlSubmitEvent::payload.name put payload
        ControlSubmitEvent::name.name put name.toString()
    }
)


public interface DataControl : ControlVision {
    /**
     * Create and dispatch submit event
     */
    public suspend fun submit(builder: MutableMeta.() -> Unit = {}) {
        dispatchControlEvent(ControlSubmitEvent(Meta(builder)))
    }
}

/**
 * Register listener
 */
public fun DataControl.onSubmit(scope: CoroutineScope, block: suspend ControlSubmitEvent.() -> Unit): Job =
    eventFlow.filterIsInstance<ControlSubmitEvent>().onEach(block).launchIn(scope)


@Serializable
@SerialName("control.valueChange")
public class ControlValueChangeEvent(override val meta: Meta) : VisionControlEvent() {

    public val value: Value? get() = meta.value

    /**
     * The name of a control that fired the event
     */
    public val name: Name? get() = meta["name"]?.string?.parseAsName()

    override fun toString(): String = meta.toString()
}


public fun ControlValueChangeEvent(value: Value?, name: Name? = null): ControlValueChangeEvent = ControlValueChangeEvent(
    Meta {
        this.value = value
        name?.let { set("name", it.toString()) }
    }
)


@Serializable
@SerialName("control.input")
public class ControlInputEvent(override val meta: Meta) : VisionControlEvent() {

    public val value: Value? get() = meta.value

    /**
     * The name of a control that fired the event
     */
    public val name: Name? get() = meta["name"]?.string?.parseAsName()

    override fun toString(): String = meta.toString()
}

public fun ControlInputEvent(value: Value?, name: Name? = null): ControlInputEvent = ControlInputEvent(
    Meta {
        this.value = value
        name?.let { set("name", it.toString()) }
    }
)
