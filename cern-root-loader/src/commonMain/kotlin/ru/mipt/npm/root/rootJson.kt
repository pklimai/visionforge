package ru.mipt.npm.root

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*


/**
 * Load Json encoded TObject
 */
public fun TObject.Companion.decodeFromJson(serializer: KSerializer<out TObject>, jsonElement: JsonElement): TObject =
    RootDecoder.decode(serializer, jsonElement)

public fun TObject.Companion.decodeFromString(serializer: KSerializer<out TObject>, string: String): TObject {
    val json = RootDecoder.json.parseToJsonElement(string)
    return RootDecoder.decode(serializer, json)
}

private object RootDecoder {

    private class RootUnrefSerializer<T>(
        private val tSerializer: KSerializer<T>,
        private val refCache: List<RefEntry>,// = ArrayList<RefEntry>(4096)
        //private val counter: ReferenceCounter
    ) : KSerializer<T> by tSerializer {

        override fun deserialize(decoder: Decoder): T {
            val input = decoder as JsonDecoder
            val element = input.decodeJsonElement()
            val refId = (element as? JsonObject)?.get("\$ref")?.jsonPrimitive?.int
            val ref = if (refId != null) {
                //println("Substituting ref $refId")
                //Forward ref for shapes
                when (tSerializer.descriptor.serialName) {
                    "TGeoShape" -> return TGeoShapeRef{
                        //Should be not null at the moment of actualization
                        refCache[refId].value as TGeoShape
                    } as T
                    "TGeoVolumeAssembly" -> return TGeoVolumeAssemblyRef{
                        //Should be not null at the moment of actualization
                        refCache[refId].value as TGeoVolumeAssembly
                    } as T
                    //Do unref
                    else -> refCache[refId]
                }
            } else {
                refCache.find { it.element == element } ?: error("Element '$element' not found in the cache")
            }

            return ref.getOrPutValue {
//                val actualTypeName = it.jsonObject["_typename"]?.jsonPrimitive?.content
                input.json.decodeFromJsonElement(tSerializer, it)
            }
        }
    }

    private fun <T> KSerializer<T>.unref(refCache: List<RefEntry>): KSerializer<T> =
        RootUnrefSerializer(this, refCache)

    @OptIn(ExperimentalSerializationApi::class)
    fun unrefSerializersModule(
        refCache: List<RefEntry>
    ): SerializersModule = SerializersModule {
        include(serializersModule)

        contextual(TGeoManager.serializer().unref(refCache))
        contextual(TObjArray.serializer().unref(refCache))
        contextual(TGeoVolumeAssembly.serializer().unref(refCache))
        contextual(TGeoShapeAssembly.serializer().unref(refCache))
        contextual(TGeoRotation.serializer().unref(refCache))
        contextual(TGeoMedium.serializer().unref(refCache))
        contextual(TGeoVolume.serializer().unref(refCache))
        contextual(TGeoMatrix.serializer().unref(refCache))
        contextual(TGeoNodeMatrix.serializer().unref(refCache))
        contextual(TGeoShape.serializer().unref(refCache))
        contextual(TObject.serializer().unref(refCache))


        polymorphicDefault(TGeoShape::class) {
            TGeoShape.serializer().unref(refCache)
        }

        polymorphicDefault(TGeoMatrix::class) {
            TGeoMatrix.serializer().unref(refCache)
        }
    }

    /**
     * Create an instance of Json with unfolding Root references. This instance could not be reused because of the cache.
     */
    private fun unrefJson(refCache: MutableList<RefEntry>): Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        classDiscriminator = "_typename"
        serializersModule = unrefSerializersModule(refCache)
    }


    fun decode(sourceDeserializer: KSerializer<out TObject>, source: JsonElement): TObject {
        val refCache = ArrayList<RefEntry>()

        fun fillCache(element: JsonElement) {
            when (element) {
                is JsonObject -> {
                    if (element["_typename"] != null) {
                        refCache.add(RefEntry(element))
                    }
                    element.values.forEach {
                        fillCache(it)
                    }
                }
                is JsonArray -> {
                    element.forEach {
                        fillCache(it)
                    }
                }
                else -> {
                    //ignore primitives
                }
            }
        }
        fillCache(source)

        return unrefJson(refCache).decodeFromJsonElement(sourceDeserializer.unref(refCache), source)
    }

//    class ReferenceCounter(var value: Int = 0) {
//        fun increment() {
//            value += 1
//        }
//
//        override fun toString(): String = value.toString()
//    }

    class RefEntry(val element: JsonElement) {

        var value: Any? = null

        fun <T> getOrPutValue(builder: (JsonElement) -> T): T {
            if (value == null) {
                value = builder(element)
            }
            return value as T
        }

        override fun toString(): String = element.toString()
    }

    private fun PolymorphicModuleBuilder<TGeoShape>.shapes() {
        subclass(TGeoBBox.serializer())
        subclass(TGeoCompositeShape.serializer())
        subclass(TGeoXtru.serializer())
        subclass(TGeoTube.serializer())
        subclass(TGeoTubeSeg.serializer())
        subclass(TGeoShapeAssembly.serializer())
    }

    private fun PolymorphicModuleBuilder<TGeoMatrix>.matrices() {
        subclass(TGeoIdentity.serializer())
        subclass(TGeoHMatrix.serializer())
        subclass(TGeoTranslation.serializer())
        subclass(TGeoRotation.serializer())
        subclass(TGeoCombiTrans.serializer())
    }

    private fun PolymorphicModuleBuilder<TGeoBoolNode>.boolNodes() {
        subclass(TGeoIntersection.serializer())
        subclass(TGeoUnion.serializer())
        subclass(TGeoSubtraction.serializer())
    }

    private val serializersModule = SerializersModule {

        polymorphic(TObject::class) {
            default { JsonRootSerializer }
            subclass(TObjArray.serializer())

            shapes()
            matrices()
            boolNodes()

            subclass(TGeoMaterial.serializer())
            subclass(TGeoMixture.serializer())

            subclass(TGeoMedium.serializer())

            subclass(TGeoNode.serializer())
            subclass(TGeoNodeMatrix.serializer())
            subclass(TGeoVolume.serializer())
            subclass(TGeoVolumeAssembly.serializer())
            subclass(TGeoManager.serializer())
        }

        polymorphic(TGeoShape::class) {
            shapes()
        }

        polymorphic(TGeoMatrix::class) {
            matrices()
        }

        polymorphic(TGeoBoolNode::class) {
            boolNodes()
        }

        polymorphic(TGeoNode::class, TGeoNode.serializer()) {
            subclass(TGeoNodeMatrix.serializer())
        }

        polymorphic(TGeoVolume::class, TGeoVolume.serializer()) {
            subclass(TGeoVolumeAssembly.serializer())
        }
    }

    val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        classDiscriminator = "_typename"
        serializersModule = this@RootDecoder.serializersModule
    }

}