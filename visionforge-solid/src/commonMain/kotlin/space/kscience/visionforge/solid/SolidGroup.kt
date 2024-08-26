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
    public fun prototypes(builder: MutableVisionGroup<Solid>.() -> Unit)

    /**
     * Resolve a prototype from this container. Should never return a ref.
     */
    public fun getPrototype(name: Name): Solid?
}

private fun MutableMap<Name, Solid>.fillFrom(prefix: Name, solids: Map<NameToken, Solid>) {
    solids.forEach { (token, solid) ->
        if(!token.body.startsWith("@")) {
            put(prefix + token, solid)
            if (solid is SolidGroup) {
                fillFrom(prefix + token, solid.solids)
            }
        }
    }
}

/**
 * A [Solid] group with additional accessor methods
 */
@Serializable
@SerialName("group.solid")
public class SolidGroup : AbstractVision(), Solid, PrototypeHolder, MutableVisionGroup<Solid> {

    private val _solids = LinkedHashMap<NameToken, Solid>()

    public val solids: Map<NameToken, Solid> get() = _solids

    /**
     * All items in this [SolidGroup] excluding invisible items (starting with @)
     */
    override val items: Map<Name, Solid> get() = buildMap { fillFrom(Name.EMPTY, solids) }

    /**
     * Get a child solid with given relative [name] if it exists
     */
    public operator fun get(name: Name): Solid? = items[name] as? Solid

    private var prototypes: SolidGroup?
        get() = _solids[PROTOTYPES_TOKEN] as? SolidGroup
        set(value) {
            if (value == null) {
                _solids.remove(PROTOTYPES_TOKEN)
            } else {
                _solids[PROTOTYPES_TOKEN] = value
            }
        }

    override val descriptor: MetaDescriptor get() = Solid.descriptor

    /**
     * Get a prototype redirecting the request to the parent if prototype is not found.
     * If a prototype is a ref, then it is unfolded automatically.
     */
    override fun getPrototype(name: Name): Solid? =
        prototypes?.get(name)?.prototype ?: (parent as? PrototypeHolder)?.getPrototype(name)

    /**
     * Create or edit prototype node as a group
     */
    override fun prototypes(builder: MutableVisionGroup<Solid>.() -> Unit): Unit {
        (prototypes ?: SolidGroup().also { prototypes = it }).apply(builder)
    }

    override fun getVision(name: Name): Solid? = when (name.length) {
        0 -> this
        1 -> solids[name.first()]
        else -> (solids[name.first()] as? SolidGroup)?.getVision(name.cutFirst())
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
            getVision(name.cutLast())
        } else {
            val parent = getOrCreateGroup(name.cutLast())
            vision.parent = parent
            parent._solids[name.last()] = vision
        }
    }

    override fun convertVisionOrNull(vision: Vision): Solid? = vision as? Solid

    public companion object {
        public val PROTOTYPES_TOKEN: NameToken = NameToken("@prototypes")
    }
}

public operator fun SolidGroup.get(name: String): Solid? = get(name.parseAsName())

public fun MutableVisionContainer<Solid>.setSolid(name: Name?, vision: Solid?) {
    if (name == null) {
        if (vision != null) {
            setVision("@static[${vision.hashCode().toString(16)}]".parseAsName(), vision)
        } else {
            //do nothing
        }
    } else {
        setVision(name, vision)
    }
}

public fun MutableVisionContainer<Solid>.setSolid(name: String?, vision: Solid?) {
    setSolid(name?.parseAsName(), vision)
}

@VisionBuilder
public inline fun MutableVisionContainer<Solid>.solidGroup(
    name: Name? = null,
    builder: SolidGroup.() -> Unit = {},
): SolidGroup = SolidGroup().also { setSolid(name, it) }.apply(builder)
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