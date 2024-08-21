package space.kscience.visionforge

import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.Value
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.get
import space.kscience.dataforge.names.*

internal class VisionProperty(
    val parent: Vision,
    val nodeName: Name,
    val inherit: Boolean,
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
                    inherit = inherit,
                    useStyles = useStyles,
                    default = default
                )
            }
        }

    override val value: Value?
        get() {
            return parent.getProperty(nodeName, inherit, useStyles).value ?: default?.getValue(nodeName)
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

/**
 * Read a property, taking into account inherited properties and styles if necessary
 */
public fun Vision.getProperty(
    name: Name,
    inherit: Boolean = descriptor?.get(name)?.inherited ?: false,
    includeStyles: Boolean = descriptor?.get(name)?.usesStyles ?: true,
): Meta = VisionProperty(this, name, inherit, includeStyles)




internal class MutableVisionProperty(
    val parent: MutableVision,
    val nodeName: Name,
    val inherit: Boolean,
    val useStyles: Boolean,
    val default: Meta? = null,
) : MutableMeta{


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
                    inherit = inherit,
                    useStyles = useStyles,
                    default = default
                )
            }
        }

    override var value: Value?
        get() {
            return parent.getProperty(nodeName, inherit, useStyles).value ?: default?.getValue(nodeName)
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
        inherit = inherit,
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
    inherit: Boolean = descriptor?.get(name)?.inherited ?: false,
    includeStyles: Boolean = descriptor?.get(name)?.usesStyles ?: true,
): MutableMeta = MutableVisionProperty(this, name, inherit, includeStyles)