package space.kscience.visionforge.solid.three

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.plus
import space.kscience.visionforge.Colors
import space.kscience.visionforge.Vision
import space.kscience.visionforge.getStyleNodes
import space.kscience.visionforge.solid.ColorAccessor
import space.kscience.visionforge.solid.SolidMaterial
import space.kscience.visionforge.solid.SolidReference
import three.materials.*
import three.math.Color
import three.objects.Mesh


public object ThreeMaterials {
    public val DEFAULT_COLOR: Color = Color(Colors.darkgreen)

    public val DEFAULT: MeshStandardMaterial = MeshStandardMaterial().apply {
        color.set(DEFAULT_COLOR)
        cached = true
    }

    public val BLACK_COLOR: Color = Color(Colors.black)

    public val DEFAULT_EMISSIVE_COLOR: Color = BLACK_COLOR

    public val DEFAULT_LINE_COLOR: Color get() = BLACK_COLOR

    public val DEFAULT_LINE: LineBasicMaterial = LineBasicMaterial().apply {
        color.set(DEFAULT_LINE_COLOR)
        cached = true
    }

    private val lineMaterialCache = HashMap<Int, LineBasicMaterial>()

    private fun buildLineMaterial(meta: Meta): LineBasicMaterial = LineBasicMaterial().apply {
        color = meta[SolidMaterial.COLOR_KEY]?.threeColor() ?: DEFAULT_LINE_COLOR
        opacity = meta[SolidMaterial.OPACITY_KEY].double ?: 1.0
        transparent = opacity < 1.0
        linewidth = meta["thickness"].double ?: 1.0
    }

    public fun getLineMaterial(meta: Meta?, cache: Boolean): LineBasicMaterial {
        if (meta == null) return DEFAULT_LINE
        return if (cache) {
            lineMaterialCache.getOrPut(meta.hashCode()) { buildLineMaterial(meta) }
        } else {
            buildLineMaterial(meta)
        }
    }

    internal fun buildMaterial(meta: Meta): Material = when (meta[SolidMaterial.TYPE_KEY]?.string) {
        "basic" -> MeshBasicMaterial().apply {
            color = meta[SolidMaterial.COLOR_KEY]?.threeColor() ?: DEFAULT_COLOR
            wireframe = meta[SolidMaterial.WIREFRAME_KEY].boolean ?: false
        }

        "lambert" -> MeshLambertMaterial().apply {
            color = meta[SolidMaterial.COLOR_KEY]?.threeColor() ?: DEFAULT_COLOR
            emissive = meta[SolidMaterial.EMISSIVE_COLOR_KEY]?.threeColor() ?: DEFAULT_EMISSIVE_COLOR
            wireframe = meta[SolidMaterial.WIREFRAME_KEY].boolean ?: false
        }

        "phong" -> MeshPhongMaterial().apply {
            color = meta[SolidMaterial.COLOR_KEY]?.threeColor() ?: DEFAULT_COLOR
            emissive = meta[SolidMaterial.EMISSIVE_COLOR_KEY]?.threeColor() ?: DEFAULT_EMISSIVE_COLOR
            wireframe = meta[SolidMaterial.WIREFRAME_KEY].boolean ?: false
        }

        else -> MeshStandardMaterial().apply {
            color = meta[SolidMaterial.COLOR_KEY]?.threeColor() ?: DEFAULT_COLOR
            emissive = meta[SolidMaterial.EMISSIVE_COLOR_KEY]?.threeColor() ?: DEFAULT_EMISSIVE_COLOR
            wireframe = meta[SolidMaterial.WIREFRAME_KEY].boolean ?: false
        }
    }.apply {
        opacity = meta[SolidMaterial.OPACITY_KEY]?.double ?: 1.0
        transparent = opacity < 1.0
        needsUpdate = true
    }

//    private val materialCache = HashMap<Int, Material>()
//
//    internal fun cacheMaterial(meta: Meta): Material = materialCache.getOrPut(meta.hashCode()) {
//        buildMaterial(meta).apply {
//            cached = true
//        }
//    }

    private val visionMaterialCache = HashMap<Vision, Material>()

    internal fun cacheMaterial(vision: Vision): Material = visionMaterialCache.getOrPut(vision) {
        buildMaterial(vision.readProperty(SolidMaterial.MATERIAL_KEY) ?: Meta.EMPTY).apply {
            cached = true
        }
    }
}

/**
 * Compute color
 */
public fun Meta.threeColor(): Color? {
    value?.let { value ->
        return when {
            value === Null -> null
            value.type == ValueType.NUMBER -> Color(value.int)
            else -> Color(value.string)
        }
    }
    val red = getValue(Colors.RED_KEY.asName())?.int
    val green = getValue(Colors.GREEN_KEY.asName())?.int
    val blue = getValue(Colors.BLUE_KEY.asName())?.int
    return if (red == null && green == null && blue == null) null else Color(red ?: 0, green ?: 0, blue ?: 0)
}

public fun ColorAccessor.threeColor(): Color? {
    val value = value
    return when {
        value == null -> null
        value === Null -> null
        value.type == ValueType.NUMBER -> Color(value.int)
        else -> Color(value.string)
    }
}

internal var Material.cached: Boolean
    get() = userData["cached"] == true
    set(value) {
        userData["cached"] = value
    }

public fun Mesh.setMaterial(vision: Vision) {
    if (
        vision.properties[SolidMaterial.MATERIAL_KEY] == null
        && vision.getStyleNodes(SolidMaterial.MATERIAL_KEY).isEmpty()
    ) {
        //if this is a reference, use material of the prototype
        if (vision is SolidReference) {
            ThreeMaterials.cacheMaterial(vision.prototype)
        } else {
            material = vision.parent?.let { parent ->
                //TODO cache parent material
                ThreeMaterials.buildMaterial(parent.readProperty(SolidMaterial.MATERIAL_KEY) ?: Meta.EMPTY)
            } ?: ThreeMaterials.cacheMaterial(vision)
        }
    } else {
        material = ThreeMaterials.buildMaterial(vision.readProperty(SolidMaterial.MATERIAL_KEY) ?: Meta.EMPTY)
    }
}

public fun Mesh.updateMaterialProperty(vision: Vision, propertyName: Name) {
    if (
        material.cached
        || propertyName == SolidMaterial.MATERIAL_KEY
        || propertyName == SolidMaterial.MATERIAL_KEY + SolidMaterial.TYPE_KEY
    ) {
        //generate a new material since cached material should not be changed
        setMaterial(vision)
    } else {
        when (propertyName) {
            SolidMaterial.MATERIAL_COLOR_KEY -> {
                material.asDynamic().color =
                    vision.readProperty(SolidMaterial.MATERIAL_COLOR_KEY)
                        ?.threeColor()
                        ?: ThreeMaterials.DEFAULT_COLOR
            }

            SolidMaterial.SPECULAR_COLOR_KEY -> {
                material.asDynamic().specular =
                    vision.readProperty(SolidMaterial.SPECULAR_COLOR_KEY)?.threeColor()
                        ?: ThreeMaterials.DEFAULT_COLOR
            }

            SolidMaterial.MATERIAL_EMISSIVE_COLOR_KEY -> {
                material.asDynamic().emissive = vision.readProperty(SolidMaterial.MATERIAL_EMISSIVE_COLOR_KEY)
                    ?.threeColor()
                    ?: ThreeMaterials.BLACK_COLOR
            }

            SolidMaterial.MATERIAL_OPACITY_KEY -> {
                val opacity = vision.readProperty(
                    SolidMaterial.MATERIAL_OPACITY_KEY,
                    inherited = true,
                )?.double ?: 1.0
                material.opacity = opacity
                material.transparent = opacity < 1.0
            }

            SolidMaterial.MATERIAL_WIREFRAME_KEY -> {
                material.asDynamic().wireframe = vision.readProperty(
                    SolidMaterial.MATERIAL_WIREFRAME_KEY,
                    inherited = true,
                )?.boolean == true
            }

            else -> console.warn("Unrecognized material property: $propertyName")
        }

        material.needsUpdate = true
    }
}