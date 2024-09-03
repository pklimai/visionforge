package space.kscience.visionforge

import com.benasher44.uuid.uuid4
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.parseAsName

@DslMarker
public annotation class VisionBuilder

/**
 * A container interface with read access to its content
 * using DataForge [Name] objects as keys.
 */
public interface VisionContainer<out V : Vision> {
    public fun getVision(name: Name): V?
}

public fun <V : Vision> VisionContainer<V>.getVision(name: String): V? = getVision(name.parseAsName())

/**
 * A container interface with write/replace/delete access to its content.
 */
public interface MutableVisionContainer<in V : Vision> {
    //TODO add documentation
    public fun setVision(name: Name, vision: V?)

    public companion object {
        public fun generateID(): String = "@vision[${uuid4().leastSignificantBits.toString(16)}]"
    }
}

public fun <V : Vision> MutableVisionContainer<V>.setVision(name: String, vision: V?) {
    setVision(name.parseAsName(), vision)
}
