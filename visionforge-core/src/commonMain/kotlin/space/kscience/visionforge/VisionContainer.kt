package space.kscience.visionforge

import com.benasher44.uuid.uuid4
import space.kscience.dataforge.names.Name

@DslMarker
public annotation class VisionBuilder

/**
 * A container interface with read access to its content
 * using DataForge [Name] objects as keys.
 */
public interface VisionContainer<out V : Vision> {
    public fun getVision(name: Name): V?
}

/**
 * A container interface with write/replace/delete access to its content.
 */
public interface MutableVisionContainer<in V : Vision> {
    //TODO add documentation
    public fun setVision(name: Name, vision: V?)

    public companion object{
        public fun generateID(): String = "@vision[${uuid4().leastSignificantBits.toString(16)}]"
    }
}