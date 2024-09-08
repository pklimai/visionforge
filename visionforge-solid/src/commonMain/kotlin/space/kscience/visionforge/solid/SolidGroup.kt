package space.kscience.visionforge.solid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.names.*
import space.kscience.visionforge.*


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

private fun MutableMap<Name, Solid>.fillFrom(prefix: Name, solids: Map<NameToken, Solid>) {
    solids.forEach { (token, solid) ->
        put(prefix + token, solid)
        if (solid is SolidGroup) {
            fillFrom(prefix + token, solid.solids)
        }
    }
}

public interface SolidContainer : VisionContainer<Solid> {

    public val solids: Map<NameToken, Solid>

    public operator fun get(token: NameToken): Solid? = solids[token]

    override fun getVision(name: Name): Solid? = when (name.length) {
        0 -> this as? Solid
        1 -> get(name.first())
        else -> (get(name.first()) as? SolidContainer)?.get(name.cutFirst())
    }
}

public operator fun SolidContainer.get(name: Name): Solid? = getVision(name)

/**
 * A [Solid] group with additional accessor methods
 */
@Serializable
@SerialName("group.solid")
public class SolidGroup : AbstractVision(), Solid, PrototypeHolder, SolidContainer, MutableVisionGroup<Solid> {

    private val _solids = LinkedHashMap<NameToken, Solid>()

    override val solids: Map<NameToken, Solid> get() = _solids

    override val items: Map<Name, Solid>
        get() = solids.mapKeys { it.key.asName() }

    private var prototypes: SolidGroup? = null

    override val descriptor: MetaDescriptor get() = Solid.descriptor

    override fun getVision(name: Name): Solid? = when (name.length) {
        0 -> this
        1 -> solids[name.first()]
        else -> (solids[name.first()] as? SolidGroup)?.get(name.cutFirst())
    }

    override fun convertVisionOrNull(vision: Vision): Solid? = vision as? Solid

    /**
     * Get a prototype redirecting the request to the parent if prototype is not found.
     * If a prototype is a ref, then it is unfolded automatically.
     */
    override fun getPrototype(name: Name): Solid? =
        prototypes?.get(name)?.prototype ?: (parent as? PrototypeHolder)?.getPrototype(name)

    /**
     * Create or edit prototype node as a group
     */
    override fun prototypes(builder: SolidGroup.() -> Unit): Unit {
        (prototypes ?: SolidGroup().also {
            prototypes = it
            parent = this
        }).apply(builder)
    }

    private fun getOrCreateGroup(name: Name): SolidGroup = when (name.length) {
        0 -> this
        1 -> when (val existing = solids[name.first()]) {
            null -> SolidGroup().also {
                it.parent = this
                _solids[name.first()] = it
            }

            is SolidGroup -> existing
            else -> error("Can't create group with name $name because non-group Solid with this name exists")
        }

        else -> getOrCreateGroup(name.first().asName()).getOrCreateGroup(name.cutFirst())
    }

    override fun setVision(name: Name, vision: Solid?) {
        if (name.isEmpty()) error("Can't set vision with empty name")
        if (vision == null) {
            (get(name.cutLast()) as SolidGroup)._solids.remove(name.last())
        } else {
            val parent = getOrCreateGroup(name.cutLast())
            vision.parent = parent
            parent._solids[name.last()] = vision
        }
        emitEvent(VisionGroupCompositionChangedEvent(this, name))
    }

    public companion object {
        public const val STATIC_TOKEN_BODY: String = "@static"

        public fun staticNameFor(vision: Vision): Name = NameToken(STATIC_TOKEN_BODY, vision.hashCode().toString(16)).asName()
        public fun inferNameFor(name: String?, vision: Vision): Name = name?.parseAsName() ?: staticNameFor(vision)
    }
}

public operator fun SolidGroup.get(name: String): Solid? = get(name.parseAsName())

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