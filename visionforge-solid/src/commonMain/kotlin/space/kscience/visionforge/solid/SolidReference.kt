package space.kscience.visionforge.solid

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import space.kscience.dataforge.meta.*
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.names.*
import space.kscience.dataforge.provider.Path
import space.kscience.visionforge.*
import space.kscience.visionforge.solid.SolidReference.Companion.REFERENCE_CHILD_PROPERTY_PREFIX


/**
 * Get a vision prototype if it is a [SolidReference] or vision itself if it is not.
 * Unref is recursive, so it always returns a non-reference.
 */
@Suppress("RecursivePropertyAccessor")
public val Vision.prototype: Solid
    get() = when (this) {
        is SolidReference -> prototype
        is SolidReferenceChild -> prototype
        is Solid -> this
        else -> error("This Vision is neither Solid nor SolidReference")
    }

public object PathSerializer : KSerializer<Path> {

    override val descriptor: SerialDescriptor
        get() = String.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Path) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Path {
        return Path.parse(decoder.decodeString())
    }
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

    override val visions: Map<NameToken, Solid>
        get() = (prototype as? SolidContainer)?.visions?.mapValues { (key, _) ->
            SolidReferenceChild(this@SolidReference, this@SolidReference, key.asName())
        } ?: emptyMap()


    override fun getVision(token: NameToken): Solid? = (prototype as? SolidContainer)?.getVision(token)?.let {
        SolidReferenceChild(this@SolidReference, it, token.asName())
    }


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

    override fun mutableProperty(
        name: Name,
        inherited: Boolean,
        useStyles: Boolean
    ): MutableMeta {
        val mutable = properties.getOrCreate(name)

        return mutable.withDefault { suffix ->
            val propertyName = name + suffix

            //2. Resolve prototype own properties
            prototype.properties[propertyName]?.let { return@withDefault it }

            if (useStyles) {
                //3. own styles
                getStyleProperty(propertyName)?.let { return@withDefault it }
                //4. prototype styles
                prototype.getStyleProperty(propertyName)?.let { return@withDefault it }
            }

            if (inherited) {
                //5. own inheritance
                parent?.readProperty(propertyName, inherited, useStyles)?.let { return@withDefault it }
                //6. prototype inheritance
                prototype.parent?.readProperty(propertyName, inherited, useStyles)?.let { return@withDefault it }
            }

            descriptor.defaultNode[name]
        }
    }

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
            VisionPropertyChangedEvent(it.propertyName.cutFirst(), it.propertyValue)
        }

    override val properties: MutableMeta = owner.properties.view(childToken.asName())

    override fun mutableProperty(
        name: Name,
        inherited: Boolean,
        useStyles: Boolean
    ): MutableMeta {
        val mutable = properties.getOrCreate(name)
        return mutable.withDefault { suffix ->
            val propertyName = name + suffix

            //2. Resolve prototype own properties
            prototype.properties[propertyName]?.let { return@withDefault it }

            if (useStyles) {
                //3. own styles
                getStyleProperty(propertyName)?.let { return@withDefault it }
                //4. prototype styles
                prototype.getStyleProperty(propertyName)?.let { return@withDefault it }
            }

            if (inherited) {
                //5. own inheritance
                parent?.readProperty(propertyName, inherited, useStyles)?.let { return@withDefault it }
                //6. prototype inheritance
                prototype.parent?.readProperty(propertyName, inherited, useStyles)?.let { return@withDefault it }
            }

            descriptor.defaultNode[name]
        }
    }

    override fun readProperty(
        name: Name,
        inherited: Boolean,
        useStyles: Boolean
    ): Meta? {
        println("read reference property")
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

    override val visions: Map<NameToken, Solid>
        get() = (prototype as? SolidContainer)?.visions?.mapValues { (key, _) ->
            SolidReferenceChild(owner, this, childName + key)
        } ?: emptyMap()

    override fun getVision(token: NameToken): Solid? = (prototype as? SolidContainer)?.getVision(token)?.let {
        SolidReferenceChild(owner, this, childName + token)
    }

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
    token: NameToken? = null,
): SolidReference = SolidReference(templateName).also {
    setVision(token ?: SolidGroup.staticNameFor(it), it)
}

public fun MutableVisionContainer<Solid>.ref(
    templateName: Name,
    name: String,
): SolidReference = ref(templateName, NameToken.parse(name))

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
            set(prototypeName, obj)
        }
    } else if (existing != obj) {
        error("Can't add different prototype on top of existing one")
    }
    return ref(prototypeName, name?.let { NameToken.parse(it) })
}