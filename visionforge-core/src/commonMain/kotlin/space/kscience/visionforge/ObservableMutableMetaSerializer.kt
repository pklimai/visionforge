package space.kscience.visionforge

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.ObservableMutableMeta
import space.kscience.dataforge.meta.asObservable

public object ObservableMutableMetaSerializer : KSerializer<ObservableMutableMeta> {
    private val serializer = MutableMeta.serializer()

    override val descriptor: SerialDescriptor get() = serializer.descriptor

    override fun deserialize(decoder: Decoder): ObservableMutableMeta {
        return decoder.decodeSerializableValue(serializer).asObservable()
    }

    override fun serialize(encoder: Encoder, value: ObservableMutableMeta) {
        encoder.encodeSerializableValue(serializer, value)
    }

}