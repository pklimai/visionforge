package space.kscience.visionforge

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
    @EncodeDefault(EncodeDefault.Mode.NEVER) final override val properties: ObservableMutableMeta = ObservableMutableMeta(),
) : MutableVision {

    private val _eventFlow by lazy {
        val scope = manager?.context ?: error("Can't observe orphan vision")
        MutableSharedFlow<VisionEvent>().also { flow ->
            properties.onChange(flow) {
                scope.launch {
                    flow.emit(VisionPropertyChangedEvent(this@AbstractVision, it))
                }
            }

            flow.onCompletion {
                properties.removeListener(flow)
            }
        }
    }

    protected fun emitEvent(event: VisionEvent) {
        manager?.context?.launch {
            _eventFlow.emit(event)
        }
    }

    override val eventFlow: SharedFlow<VisionEvent> get() = _eventFlow

    @Transient
    override var parent: Vision? = null
        set(value) {
            if (value == null) {
                error("Can't remove parent for existing vision")
            } else if (field != null) {
                error("Parent is already set")
            } else {
                field = value
            }
        }

    override val descriptor: MetaDescriptor? get() = null

}