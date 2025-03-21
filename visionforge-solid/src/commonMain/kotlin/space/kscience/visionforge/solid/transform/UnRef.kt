package space.kscience.visionforge.solid.transform

import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.solid.SolidGroup
import space.kscience.visionforge.solid.SolidReference
import space.kscience.visionforge.solid.set

@DFExperimental
internal object UnRef : VisualTreeTransform<SolidGroup>() {
    private fun SolidGroup.countRefs(): Map<Name, Int> {
        return visions.values.fold(HashMap()) { reducer, vision ->
            if (vision is SolidGroup) {
                val counter = vision.countRefs()
                counter.forEach { (key, value) ->
                    reducer[key] = (reducer[key] ?: 0) + value
                }
            } else if (vision is SolidReference) {
                reducer[vision.prototypeName] = (reducer[vision.prototypeName] ?: 0) + 1
            }

            return reducer
        }
    }

    private fun SolidGroup.unref(name: Name) {
        prototypes {
            set(name, null)
        }
        visions.filter { (it.value as? SolidReference)?.prototypeName == name }.forEach { (key, value) ->
            val reference = value as SolidReference
            val newChild = reference.prototype.updateFrom(reference)
            newChild.parent = null
            setVision(key, newChild) // replace proxy with merged object
        }

        visions.values.filterIsInstance<SolidGroup>().forEach { it.unref(name) }
    }

    override fun SolidGroup.transformInPlace() {
        val counts = countRefs()
        counts.filter { it.value <= 1 }.forEach {
            this.unref(it.key)
        }
    }

    override fun SolidGroup.clone(): SolidGroup {
        TODO()
    }

}