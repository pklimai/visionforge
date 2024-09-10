package space.kscience.visionforge

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import space.kscience.dataforge.meta.ObservableMutableMeta
import space.kscience.dataforge.meta.descriptors.MetaDescriptor


@Serializable
public abstract class AbstractVision(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @Serializable(ObservableMutableMetaSerializer::class)
    final override val properties: ObservableMutableMeta = ObservableMutableMeta(),
) : MutableVision {

    @Transient
    private val _eventFlow = MutableSharedFlow<VisionEvent>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    protected fun emitEvent(event: VisionEvent) {
        val context = manager?.context
        if (context == null) {
            _eventFlow.tryEmit(event)
        } else {
            context.launch {
                _eventFlow.emit(event)
            }
        }
    }

    init {
        properties.onChange(_eventFlow) {
            emitEvent(VisionPropertyChangedEvent(this@AbstractVision, it))
        }

        _eventFlow.onCompletion {
            properties.removeListener(_eventFlow)
        }
    }

    override val eventFlow: SharedFlow<VisionEvent> get() = _eventFlow

    @Transient
    override var parent: Vision? = null
        set(value) {
            if (parent != null && field != null) {
                error("Parent is already set")
            } else {
                field = value
            }
        }

    override val descriptor: MetaDescriptor? get() = null

}