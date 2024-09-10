package space.kscience.visionforge.solid

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import space.kscience.dataforge.meta.Laminate
import space.kscience.dataforge.meta.Meta
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.MutableMetaProxy
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.names.*
import space.kscience.visionforge.*
import space.kscience.visionforge.solid.SolidReference.Companion.REFERENCE_CHILD_PROPERTY_PREFIX


/**
 * Get a vision prototype if it is a [SolidReference] or vision itself if it is not.
 * Unref is recursive, so it always returns a non-reference.
 */
@Suppress("RecursivePropertyAccessor")
public val Vision.prototype: Solid
    get() = when (this) {
        is SolidReference -> prototype.prototype
        is SolidReferenceChild -> prototype.prototype
        is Solid -> this
        else -> error("This Vision is neither Solid nor SolidReference")
    }

@Serializable
@SerialName("solid.ref")
public class SolidReference(
    @SerialName("prototype") public val prototypeName: Name,
) : AbstractVision(), SolidContainer {

    @Transient
    override var parent: Vision? = null

    /**
     * The prototype for this reference.
     */
    public val prototype: Solid by lazy {
        //Recursively search for defined template in the parent
        if (parent == null) error("No parent is present for SolidReference")
        if (parent !is PrototypeHolder) error("Parent does not hold prototypes")
        (parent as? PrototypeHolder)?.getPrototype(prototypeName)
            ?: error("Prototype with name $prototypeName not found")
    }

    override val descriptor: MetaDescriptor get() = prototype.descriptor

    override val items: Map<Name, Solid>
        get() = (prototype as? SolidContainer)?.items?.mapValues { (key, _) ->
            SolidReferenceChild(this@SolidReference, this@SolidReference, key)
        } ?: emptyMap()


    override fun getVision(name: Name): Solid? = (prototype as? SolidContainer)?.getVision(name)


    override fun readProperty(
        name: Name,
        inherited: Boolean,
        useStyles: Boolean
    ): Meta? {
        val listOfMeta = buildList {
            //1. resolve own properties
            add(properties[name])
            //2. Resolve prototype own properties
            add(prototype.properties[name])

            if (useStyles) {
                //3. own styles
                add(getStyleProperty(name))
                //4. prototype styles
                add(prototype.getStyleProperty(name))
            }

            if (inherited) {
                //5. own inheritance
                add(parent?.readProperty(name, inherited, useStyles))
                //6. prototype inheritance
                add(prototype.parent?.readProperty(name, inherited, useStyles))
            }

            add(descriptor.defaultNode[name])
        }

        return if (listOfMeta.all { it == null }) null else Laminate(listOfMeta)
    }

    //
//    override val properties: MutableVisionProperties by lazy {
//        object : AbstractVisionProperties(this, propertiesInternal) {
//
//            override fun getValue(name: Name, inherit: Boolean?, includeStyles: Boolean?): Value? {
//                if (name == Vision.STYLE_KEY) {
//                    return buildList {
//                        own.getValue(Vision.STYLE_KEY)?.list?.forEach {
//                            add(it)
//                        }
//                        prototype.styles.forEach {
//                            add(it.asValue())
//                        }
//                    }.distinct().asValue()
//                }
//                //1. resolve own properties
//                own.getValue(name)?.let { return it }
//
//                val descriptor = descriptor?.get(name)
//                val inheritFlag = inherit ?: descriptor?.inherited ?: false
//                val stylesFlag = includeStyles ?: descriptor?.usesStyles ?: true
//
//                //2. Resolve prototype onw properties
//                prototype.properties.own.getValue(name)?.let { return it }
//
//                if (stylesFlag) {
//                    //3. own styles
//                    own.getValue(Vision.STYLE_KEY)?.list?.forEach { styleName ->
//                        getStyle(styleName.string)?.getValue(name)?.let { return it }
//                    }
//                    //4. prototype styles
//                    prototype.getStyleProperty(name)?.value?.let { return it }
//                }
//
//                if (inheritFlag) {
//                    //5. own inheritance
//                    parent?.properties?.getValue(name, inheritFlag, includeStyles)?.let { return it }
//                    //6. prototype inheritance
//                    prototype.parent?.properties?.getValue(name, inheritFlag, includeStyles)?.let { return it }
//                }
//
//                return descriptor?.defaultValue
//            }
//
//
//            override fun invalidate(propertyName: Name) {
//                //send update signal
//                @OptIn(DelicateCoroutinesApi::class)
//                (manager?.context ?: GlobalScope).launch {
//                    changesInternal.emit(propertyName)
//                }
//
//                // update styles
//                if (propertyName == Vision.STYLE_KEY) {
//                    styles.asSequence()
//                        .mapNotNull { getStyle(it) }
//                        .flatMap { it.items.asSequence() }
//                        .distinctBy { it.key }
//                        .forEach {
//                            invalidate(it.key.asName())
//                        }
//                }
//            }
//        }
//    }
//
//    val items: VisionChildren
//        get() = object : VisionChildren {
//            override val parent: Vision get() = this@SolidReference
//
//            override val keys: Set<NameToken> get() = prototype.children?.keys ?: emptySet()
//
//            override val changes: Flow<Name> get() = emptyFlow()
//
//            override fun get(token: NameToken): SolidReferenceChild? {
//                if (token !in (prototype.children?.keys ?: emptySet())) return null
//                return SolidReferenceChild(this@SolidReference, this@SolidReference, token.asName())
//            }
//        }

    public companion object {
        public const val REFERENCE_CHILD_PROPERTY_PREFIX: String = "@child"
    }
}

/**
 * @param childName A name of reference child relative to prototype root
 */
private class SolidReferenceChild(
    val owner: SolidReference,
    override var parent: Vision?,
    val childName: Name,
) : SolidContainer {

    private val childToken = childToken(childName)

    val prototype: Solid
        get() = (owner.prototype as SolidContainer).getVision(childName)
            ?: error("Prototype with name $childName not found")

    override val descriptor: MetaDescriptor get() = prototype.descriptor

    override val eventFlow: Flow<VisionEvent>
        get() = owner.eventFlow.filterIsInstance<VisionPropertyChangedEvent>().filter {
            it.propertyName.startsWith(childToken)
        }.map {
            VisionPropertyChangedEvent(this@SolidReferenceChild, it.propertyName.cutFirst())
        }


    override val properties: MutableMeta = MutableMetaProxy(owner.properties, childToken.asName())

    override fun readProperty(
        name: Name,
        inherited: Boolean,
        useStyles: Boolean
    ): Meta? {
        val listOfMeta = buildList {
            //1. resolve own properties
            add(properties[name])
            //2. Resolve prototype own properties
            add(prototype.properties[name])

            if (useStyles) {
                //3. own styles
                add(getStyleProperty(name))
                //4. prototype styles
                add(prototype.getStyleProperty(name))
            }

            if (inherited) {
                //5. own inheritance
                add(parent?.readProperty(name, inherited, useStyles))
                //6. prototype inheritance
                add(prototype.parent?.readProperty(name, inherited, useStyles))
            }

            add(descriptor.defaultNode[name])
        }

        return if (listOfMeta.all { it == null }) null else Laminate(listOfMeta)
    }

    override val items: Map<Name, Solid>
        get() = (prototype as? SolidContainer)?.items?.mapValues { (key, _) ->
            SolidReferenceChild(owner, this, childName + key)
        } ?: emptyMap()

    //    @Transient
//    override val properties: MutableVisionProperties = object : MutableVisionProperties {
//        override val descriptor: MetaDescriptor get() = this@SolidReferenceChild.descriptor
//
//        override val own: MutableMeta by lazy { owner.properties[childToken(childName).asName()] }
//
//        override fun getValue(
//            name: Name,
//            inherit: Boolean?,
//            includeStyles: Boolean?,
//        ): Value? = own.getValue(name) ?: prototype.properties.getValue(name, inherit, includeStyles)
//
//        override fun set(name: Name, item: Meta?, notify: Boolean) {
//            own[name] = item
//        }
//
//        override fun setValue(name: Name, value: Value?, notify: Boolean) {
//            own.setValue(name, value)
//        }
//
//        override val changes: Flow<Name>
//            get() = owner.properties.changes.filter { it.startsWith(childToken(childName)) }
//
//        override fun invalidate(propertyName: Name) {
//            owner.properties.invalidate(childPropertyName(childName, propertyName))
//        }
//    }

    companion object {

        private fun childToken(childName: Name): NameToken =
            NameToken(REFERENCE_CHILD_PROPERTY_PREFIX, childName.toString())

        private fun childPropertyName(childName: Name, propertyName: Name): Name =
            childToken(childName) + propertyName

    }
}

/**
 * Create ref for existing prototype
 */
public fun MutableVisionContainer<Solid>.ref(
    templateName: Name,
    name: Name? = null,
): SolidReference = SolidReference(templateName).also {
    setVision(name ?: SolidGroup.staticNameFor(it), it)
}

public fun MutableVisionContainer<Solid>.ref(
    templateName: Name,
    name: String,
): SolidReference = ref(templateName, name.parseAsName())

/**
 * Add new [SolidReference] wrapping given object and automatically adding it to the prototypes.
 */
public fun SolidGroup.newRef(
    name: String?,
    obj: Solid,
    prototypeHolder: SolidGroup = this,
    prototypeName: Name = Name.parse(name ?: obj.toString()),
): SolidReference {
    val existing = prototypeHolder.getPrototype(prototypeName)
    if (existing == null) {
        prototypeHolder.prototypes {
            setVision(prototypeName, obj)
        }
    } else if (existing != obj) {
        error("Can't add different prototype on top of existing one")
    }
    return ref(prototypeName, name?.parseAsName())
}