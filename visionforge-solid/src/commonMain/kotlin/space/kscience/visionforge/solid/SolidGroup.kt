package space.kscience.visionforge.solid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.NameToken
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.parseAsName
import space.kscience.dataforge.provider.Path
import space.kscience.dataforge.provider.length
import space.kscience.dataforge.provider.provide
import space.kscience.dataforge.provider.tail
import space.kscience.visionforge.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.Map
import kotlin.collections.first
import kotlin.collections.set


/**
 * A container with prototype support
 */
public interface PrototypeHolder {
    /**
     * Build or update the prototype tree
     */
    @VisionBuilder
    public fun prototypes(builder: SolidGroup.() -> Unit)

    /**
     * Resolve a prototype from this container. Should never return a ref.
     */
    public fun getPrototype(name: Name): Solid?
}

public interface SolidContainer : VisionGroup<Solid>, Solid {
    override suspend fun receiveEvent(event: VisionEvent) {
        super<VisionGroup>.receiveEvent(event)
    }
}

/**
 * A [Solid] group with additional accessor methods
 */
@Serializable
@SerialName("group.solid")
public class SolidGroup : AbstractVision(), SolidContainer, PrototypeHolder, MutableVisionGroup<Solid> {

    private val _solids = LinkedHashMap<Name, Solid>()

    override val items: Map<Name, Solid> get() = _solids

    private var prototypes: SolidGroup? = null

    override val descriptor: MetaDescriptor get() = Solid.descriptor

    override fun convertVisionOrNull(vision: Vision): Solid? = vision as? Solid

    override suspend fun receiveEvent(event: VisionEvent) {
        super<MutableVisionGroup>.receiveEvent(event)
    }

    /**
     * Get a prototype redirecting the request to the parent if prototype is not found.
     * If a prototype is a ref, then it is unfolded automatically.
     */
    override fun getPrototype(name: Name): Solid? =
        prototypes?.getVision(name)?.prototype ?: (parent as? PrototypeHolder)?.getPrototype(name)

    /**
     * Create or edit prototype node as a group
     */
    override fun prototypes(builder: SolidGroup.() -> Unit): Unit {
        (prototypes ?: SolidGroup().also {
            prototypes = it
            parent = this
        }).apply(builder)
    }

    override val defaultChainTarget: String get() = VisionGroup.VISION_CHILD_TARGET

    override fun setVision(name: Name, vision: Solid?) {
        if (vision == null) {
            _solids.remove(name)?.let {
                it.parent = null
            }
        } else {
            _solids[name] = vision
            vision.parent = this
        }
        emitEvent(VisionGroupCompositionChangedEvent(this, name))
    }

    public companion object {
        public const val STATIC_TOKEN_BODY: String = "@static"

        public fun staticNameFor(vision: Vision): Name =
            NameToken(STATIC_TOKEN_BODY, vision.hashCode().toString(16)).asName()

        public fun inferNameFor(name: String?, vision: Vision): Name = name?.parseAsName() ?: staticNameFor(vision)
    }
}

public operator fun SolidContainer.get(name: Name): Solid? = getVision(name)

public operator fun SolidContainer.get(name: String): Solid? = get(name.parseAsName())

public operator fun SolidContainer.get(path: Path): Solid? =
    provide(path, VisionGroup.VISION_CHILD_TARGET) as? Solid

public operator fun SolidGroup.set(path: Path, vision: Solid?) {
    when (path.length) {
        0 -> error("Can't set vision with empty path")
        1 -> {
            val token = path.first()
            check (token.target != null && token.target != VisionGroup.VISION_CHILD_TARGET) {
                "Unsupported target: ${token.target}"
            }
            setVision(token.name, vision)
        }

        else -> {
            val token = path.first()
            check (token.target != null && token.target != VisionGroup.VISION_CHILD_TARGET) {
                "Unsupported target: ${token.target}"
            }
            when (val existing = getVision(token.name)) {
                null -> SolidGroup().also { newGroup ->
                    setVision(token.name, newGroup)
                    newGroup[path.tail!!] = vision
                }
                is SolidGroup -> existing[path.tail!!] = vision

                else -> error("Can't set Solid by path because of existing non-group Solid at $token")
            }
        }
    }
}

/**
 * Add anonymous (auto-assigned name) child to a SolidGroup
 */
public fun MutableVisionContainer<Solid>.static(solid: Solid) {
    setVision(SolidGroup.staticNameFor(solid), solid)
}

@VisionBuilder
public inline fun MutableVisionContainer<Solid>.solidGroup(
    name: Name? = null,
    builder: SolidGroup.() -> Unit = {},
): SolidGroup = SolidGroup().also {
    setVision(name ?: SolidGroup.staticNameFor(it), it)
}.apply(builder)
//root first, update later

/**
 * Define a group with given [name], attach it to this parent and return it.
 */
@VisionBuilder
public inline fun MutableVisionContainer<Solid>.solidGroup(
    name: String,
    action: SolidGroup.() -> Unit = {},
): SolidGroup = solidGroup(name.parseAsName(), action)

/**
 * Create a [SolidGroup] using given configuration [block]
 */
public inline fun SolidGroup(block: SolidGroup.() -> Unit): SolidGroup = SolidGroup().apply(block)