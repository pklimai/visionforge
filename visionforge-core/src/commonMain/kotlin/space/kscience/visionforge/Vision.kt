package space.kscience.visionforge

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import space.kscience.dataforge.meta.asValue
import space.kscience.dataforge.meta.boolean
import space.kscience.dataforge.meta.descriptors.Described
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.misc.Type
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.visionforge.AbstractVisionGroup.Companion.updateProperties
import space.kscience.visionforge.Vision.Companion.TYPE

/**
 * A root type for display hierarchy
 */
@Type(TYPE)
public interface Vision : Described {

    /**
     * The parent object of this one. If null, this one is a root.
     */
    public var parent: Vision?

    /**
     * Owner [VisionManager]. Used to define coroutine scope a serialization
     */
    public val manager: VisionManager? get() = parent?.manager


    public val properties: MutableVisionProperties

    /**
     * Update this vision using a dif represented by [VisionChange].
     */
    public fun update(change: VisionChange) {
        change.properties?.let {
            updateProperties(it, Name.EMPTY)
        }
    }

    override val descriptor: MetaDescriptor?

    public companion object {
        public const val TYPE: String = "vision"
        public val STYLE_KEY: Name = "@style".asName()
        public const val STYLE_TARGET: String = "style"

        public val VISIBLE_KEY: Name = "visible".asName()
    }
}

/**
 * Control visibility of the element
 */
public var Vision.visible: Boolean?
    get() = properties.getValue(Vision.VISIBLE_KEY)?.boolean
    set(value) {
        properties.setValue(Vision.VISIBLE_KEY, value?.asValue())
    }

/**
 * Subscribe on property updates. The subscription is bound to the given scope and canceled when the scope is canceled
 */
public fun Vision.onPropertyChange(
    scope: CoroutineScope? = manager?.context,
    callback: suspend (Name) -> Unit
): Job = properties.changes.onEach {
    callback(it)
}.launchIn(scope ?: error("Orphan Vision can't observe properties"))