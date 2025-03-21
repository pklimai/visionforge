package space.kscience.visionforge.solid.transform

import space.kscience.dataforge.meta.update
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.kmath.complex.QuaternionAlgebra
import space.kscience.visionforge.properties
import space.kscience.visionforge.solid.*

private operator fun Number.plus(other: Number) = toFloat() + other.toFloat()
private operator fun Number.times(other: Number) = toFloat() * other.toFloat()

@DFExperimental
internal fun Solid.updateFrom(other: Solid): Solid {
    x += other.x
    y += other.y
    z += other.y
    quaternion = with(QuaternionAlgebra) { other.quaternion * quaternion }
    scaleX *= other.scaleX
    scaleY *= other.scaleY
    scaleZ *= other.scaleZ
    properties {
        update(other.properties)
    }
    return this
}


@DFExperimental
internal object RemoveSingleChild : VisualTreeTransform<SolidGroup>() {

    override fun SolidGroup.transformInPlace() {
        fun SolidGroup.replaceChildren() {
            visions.forEach { (childName, parent) ->
                if (parent is SolidReference) return@forEach //ignore refs
                if (parent is SolidGroup) {
                    parent.replaceChildren()
                }
                if (parent is SolidGroup && parent.visions.size == 1) {
                    val child: Solid = parent.visions.values.first()
                    val newParent = child.updateFrom(parent)
                    newParent.parent = null
                    setVision(childName, newParent)
                }
            }
        }

        replaceChildren()
        prototypes {
            replaceChildren()
        }
    }

    override fun SolidGroup.clone(): SolidGroup {
        TODO()
    }
}