package space.kscience.visionforge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.ValueType
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.value
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.parseAsName
import space.kscience.dataforge.names.plus
import space.kscience.visionforge.SimpleVisionGroup.Companion.updateProperties
import space.kscience.visionforge.Vision.Companion.STYLE_KEY
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


public interface VisionGroup<out V : Vision> : Vision, VisionContainer<V> {

    public val items: Map<Name, V>

    override fun getVision(name: Name): V? = items[name]

    override suspend fun receiveEvent(event: VisionEvent) {
        super.receiveEvent(event)
        if (event is VisionChange) {
            event.children?.forEach { (name, change) ->
                if (event.vision != null) {
                    error("VisionGroup is read-only")
                } else {
                    getVision(name)?.receiveEvent(change)
                }
            }
        }
    }
}

/**
 * An event that indicates that [VisionGroup] composition is invalidated (not necessarily changed
 */
public data class VisionGroupCompositionChangedEvent(
    public val source: VisionContainer<*>,
    public val childName: Name
) : VisionEvent

///**
// * An event that indicates that child property value has been invalidated
// */
//public data class VisionGroupPropertyChangedEvent(
//    public val source: VisionGroup<*>,
//    public val childName: Name,
//    public val propertyName: Name
//) : VisionEvent

public interface MutableVisionGroup<V : Vision> : VisionGroup<V>, MutableVision, MutableVisionContainer<V> {

    /**
     * This method tries to convert a [vision] to the typed vision handled by this [MutableVisionGroup].
     * Return null if conversion is failed.
     */
    public fun convertVisionOrNull(vision: Vision): V?

    override suspend fun receiveEvent(event: VisionEvent) {
        if (event is VisionChange) {
            event.properties?.let {
                updateProperties(it, Name.EMPTY)
            }
            event.children?.forEach { (name, change) ->
                change.children?.forEach { (name, change) ->
                    when {
                        change.vision == NullVision -> setVision(name, null)
                        change.vision != null -> setVision(
                            name,
                            convertVisionOrNull(change.vision) ?: error("Can't convert ${change.vision}")
                        )

                        else -> getVision(name)?.receiveEvent(change)
                    }
                }
                change.properties?.let {
                    updateProperties(it, Name.EMPTY)
                }
            }
        }
    }
}

/**
 * A simple vision group that just holds children. Nothing else.
 */
@Serializable
@SerialName("vision.group")
public class SimpleVisionGroup : AbstractVision(), MutableVisionGroup<Vision> {

    @Serializable
    @SerialName("children")
    private val _items = mutableMapOf<Name, Vision>()

    override val items: Map<Name, Vision> get() = _items

    override fun convertVisionOrNull(vision: Vision): Vision = vision

    override fun setVision(name: Name, vision: Vision?) {
        if (vision == null) {
            _items.remove(name)
        } else {
            _items[name] = vision
            vision.parent = this
        }
        emitEvent(VisionGroupCompositionChangedEvent(this, name))
    }

    public companion object {
        public val descriptor: MetaDescriptor = MetaDescriptor {
            value(STYLE_KEY, ValueType.STRING) {
                multiple = true
            }
        }

        public fun MutableVision.updateProperties(item: Meta, name: Name = Name.EMPTY) {
            properties.setValue(name, item.value)
            item.items.forEach { (token, item) ->
                updateProperties(item, name + token)
            }
        }

    }
}

@VisionBuilder
public inline fun MutableVisionContainer<Vision>.group(
    name: Name? = null,
    builder: SimpleVisionGroup.() -> Unit = {},
): SimpleVisionGroup = SimpleVisionGroup().also {
    setVision(name ?: MutableVisionContainer.generateID().asName(), it)
}.apply(builder)

/**
 * Define a group with given [name], attach it to this parent and return it.
 */
@VisionBuilder
public inline fun MutableVisionContainer<Vision>.group(
    name: String,
    builder: SimpleVisionGroup.() -> Unit = {},
): SimpleVisionGroup = group(name.parseAsName(), builder)

public fun VisionGroup(
    block: MutableVisionGroup<Vision>.() -> Unit
): VisionGroup<Vision> = SimpleVisionGroup().apply(block)

//fun VisualObject.findStyle(styleName: Name): Meta? {
//    if (this is VisualGroup) {
//        val style = resolveStyle(styleName)
//        if (style != null) return style
//    }
//    return parent?.findStyle(styleName)
//}