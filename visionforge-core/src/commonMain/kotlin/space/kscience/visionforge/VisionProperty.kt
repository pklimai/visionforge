package space.kscience.visionforge

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.get
import space.kscience.dataforge.names.*

private class VisionProperty(
    val parent: Vision,
    val nodeName: Name,
    val inherited: Boolean,
    val useStyles: Boolean,
    val default: Meta? = null,
) : Meta {

    val descriptor: MetaDescriptor? by lazy { parent.descriptor?.get(nodeName) }


    override val items: Map<NameToken, VisionProperty>
        get() {
            val metaKeys = parent.properties[nodeName]?.items?.keys ?: emptySet()
            val descriptorKeys = descriptor?.nodes?.map { NameToken(it.key) } ?: emptySet()
            val defaultKeys = default?.get(nodeName)?.items?.keys ?: emptySet()
            return (metaKeys + descriptorKeys + defaultKeys).associateWith {
                VisionProperty(
                    parent = parent,
                    nodeName = nodeName + it,
                    inherited = inherited,
                    useStyles = useStyles,
                    default = default
                )
            }
        }

    override val value: Value?
        get() {
            return parent.getProperty(nodeName, inherited, useStyles).value ?: default?.getValue(nodeName)
        }

    //TODO remove with DataForge 0.9.1
    override fun get(name: Name): VisionProperty? {
        tailrec fun VisionProperty.find(name: Name): VisionProperty? = if (name.isEmpty()) {
            this
        } else {
            items[name.firstOrNull()!!]?.find(name.cutFirst())
        }

        return find(name)
    }

    override fun toString(): String = Meta.toString(this)
    override fun equals(other: Any?): Boolean = Meta.equals(this, other as? Meta)
    override fun hashCode(): Int = Meta.hashCode(this)
}

internal fun Vision.isInheritedProperty(name: Name): Boolean = descriptor?.get(name)?.inherited ?: false
internal fun Vision.isInheritedProperty(name: String): Boolean = descriptor?.get(name)?.inherited ?: false
internal fun Vision.isStyledProperty(name: Name): Boolean = descriptor?.get(name)?.usesStyles ?: true
internal fun Vision.isStyledProperty(name: String): Boolean = descriptor?.get(name)?.usesStyles ?: true


/**
 * Read a property, taking into account inherited properties and styles if necessary
 */
public fun Vision.getProperty(
    name: Name,
    inherited: Boolean = isInheritedProperty(name),
    useStyles: Boolean = isStyledProperty(name),
): Meta = VisionProperty(this, name, inherited, useStyles)

public fun Vision.getProperty(
    name: String,
    inherited: Boolean = isInheritedProperty(name),
    useStyles: Boolean = isStyledProperty(name),
): Meta = getProperty(name.parseAsName(), inherited, useStyles)

public fun Vision.properties(
    inherited: Boolean = false,
    useStyles: Boolean = true,
): Meta = getProperty(Name.EMPTY,inherited, useStyles)


internal class MutableVisionProperty(
    val parent: MutableVision,
    val nodeName: Name,
    val inherited: Boolean,
    val useStyles: Boolean,
    val default: Meta? = null,
) : MutableMeta {


    val descriptor: MetaDescriptor? by lazy { parent.descriptor?.get(nodeName) }


    override val items: Map<NameToken, MutableVisionProperty>
        get() {
            val metaKeys = parent.properties[nodeName]?.items?.keys ?: emptySet()
            val descriptorKeys = descriptor?.nodes?.map { NameToken(it.key) } ?: emptySet()
            val defaultKeys = default?.get(nodeName)?.items?.keys ?: emptySet()
            return (metaKeys + descriptorKeys + defaultKeys).associateWith {
                MutableVisionProperty(
                    parent = parent,
                    nodeName = nodeName + it,
                    inherited = inherited,
                    useStyles = useStyles,
                    default = default
                )
            }
        }

    override var value: Value?
        get() {
            return parent.getProperty(nodeName, inherited, useStyles).value ?: default?.getValue(nodeName)
        }
        set(value) {
            parent.properties.setValue(nodeName, value)
        }

    //TODO remove with DataForge 0.9.1
    override fun get(name: Name): MutableVisionProperty? {
        tailrec fun MutableVisionProperty.find(name: Name): MutableVisionProperty? = if (name.isEmpty()) {
            this
        } else {
            items[name.firstOrNull()!!]?.find(name.cutFirst())
        }

        return find(name)
    }


    override fun getOrCreate(name: Name): MutableVisionProperty = MutableVisionProperty(
        parent = parent,
        nodeName = nodeName + name,
        inherited = inherited,
        useStyles = useStyles,
        default = default
    )

    override fun set(name: Name, node: Meta?) {
        parent.properties[nodeName + name] = node
    }

    override fun toString(): String = Meta.toString(this)
    override fun equals(other: Any?): Boolean = Meta.equals(this, other as? Meta)
    override fun hashCode(): Int = Meta.hashCode(this)
}

public fun MutableVision.getProperty(
    name: Name,
    inherited: Boolean = isInheritedProperty(name),
    useStyles: Boolean = isStyledProperty(name),
): MutableMeta = MutableVisionProperty(this, name, inherited, useStyles)

public fun MutableVision.getProperty(
    name: String,
    inherited: Boolean = isInheritedProperty(name),
    useStyles: Boolean = isStyledProperty(name),
): MutableMeta = getProperty(name.parseAsName(), inherited, useStyles)

public fun MutableVision.properties(
    inherited: Boolean = false,
    useStyles: Boolean = true,
): MutableMeta = getProperty(Name.EMPTY,inherited, useStyles)