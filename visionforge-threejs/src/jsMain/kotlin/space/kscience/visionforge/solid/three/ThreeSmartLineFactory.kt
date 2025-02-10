package space.kscience.visionforge.solid.three

import space.kscience.visionforge.solid.PolyLine
import three.core.Object3D
import kotlin.reflect.KClass

public object ThreeSmartLineFactory : ThreeFactory<PolyLine> {
    override val type: KClass<in PolyLine> get() = PolyLine::class

    override suspend fun build(
        three: ThreePlugin,
        vision: PolyLine,
        observe: Boolean,
    ): Object3D = if (vision.thickness == PolyLine.DEFAULT_THICKNESS) {
        ThreeLineFactory.build(three, vision, observe)
    } else {
        ThreeMeshLineFactory.build(three, vision, observe)
    }
}