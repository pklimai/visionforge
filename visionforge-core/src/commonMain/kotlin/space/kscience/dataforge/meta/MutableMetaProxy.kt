package space.kscience.dataforge.meta

import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.NameToken
import space.kscience.dataforge.names.plus

public class MutableMetaProxy(
    public val upstream: MutableMeta,
    public val proxyName: Name
) : MutableMeta {

    override val items: Map<NameToken, MutableMeta>
        get() = upstream[proxyName]?.items ?: emptyMap()

    override var value: Value?
        get() = upstream[proxyName]?.value
        set(value) {
            upstream[proxyName] = value
        }

    override fun getOrCreate(name: Name): MutableMeta = upstream.getOrCreate(proxyName + name)


    override fun set(name: Name, node: Meta?) {
        set(proxyName + name, node)
    }


    override fun equals(other: Any?): Boolean = Meta.equals(this, other as? Meta)


    override fun hashCode(): Int  = Meta.hashCode(this)

    override fun toString(): String = Meta.toString(this)
}