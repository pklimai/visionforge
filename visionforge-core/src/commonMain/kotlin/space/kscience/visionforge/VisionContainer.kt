package space.kscience.visionforge

import com.benasher44.uuid.uuid4
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.NameToken

@DslMarker
public annotation class VisionBuilder

/**
 * A container interface with read access to its content
 * using DataForge [Name] objects as keys.
 */
public interface VisionContainer<out V : Vision> {
    public fun getVision(token: NameToken): V?
}

public fun <V : Vision> VisionContainer<V>.getVision(token: String): V? = getVision(NameToken.parse(token))

/**
 * A container interface with write/replace/delete access to its content.
 */
public interface MutableVisionContainer<in V : Vision> {
    //TODO add documentation
    public fun setVision(token: NameToken, vision: V?)

    public companion object {
        public fun generateID(): NameToken = NameToken("@vision",uuid4().leastSignificantBits.toString(16))
    }
}