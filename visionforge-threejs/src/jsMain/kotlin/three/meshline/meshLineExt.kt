package three.meshline

import three.core.BufferGeometry
import three.materials.Material
import three.math.Vector3

public fun MeshLine(geometry: BufferGeometry): MeshLineGeometry = MeshLineGeometry().apply { copy(geometry) }

public fun MeshLine(points: Array<Vector3>): MeshLineGeometry = MeshLineGeometry().apply { setPoints(points) }

internal fun isMeshLineMaterial(material: Material): Boolean = material.asDynamic().isMeshLineMaterial == true