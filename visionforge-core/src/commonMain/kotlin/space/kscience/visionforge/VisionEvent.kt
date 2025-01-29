package space.kscience.visionforge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MetaRepr
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.NameToken

/**
 * An event propagated from client to a server or vise versa
 */
public sealed interface VisionEvent {
    public companion object {
        public val CLICK_EVENT_KEY: Name get() = Name.of("events", "click", "payload")
    }
}

/**
 * A list of [VisionEvent] that are delivered at the same time
 */
@Serializable
public class VisionEventCollection(public val events: List<VisionEvent>): VisionEvent

/**
 * An event that should be forwarded to a [Vision] child
 */
@Serializable
public class VisionChildEvent(public val childName: Name, public val event: VisionEvent): VisionEvent



public sealed interface VisionChangedEvent: VisionEvent

/**
 * An event that designates that property value is invalidated (not necessarily changed)
 */
public data class VisionPropertyChangedEvent(
    public val source: Vision,
    public val propertyName: Name
): VisionChangedEvent

/**
 * An event that indicates that [VisionGroup] composition is invalidated (not necessarily changed)
 */
public data class VisionGroupCompositionChangedEvent(
    public val source: VisionContainer<*>,
    public val childName: NameToken
) : VisionChangedEvent

/**
 * An event that consists of custom meta
 */
@Serializable
@SerialName("meta")
public class VisionMetaEvent(public val meta: Meta) : VisionEvent, MetaRepr {
    override fun toMeta(): Meta = meta

    override fun toString(): String = toMeta().toString()

}