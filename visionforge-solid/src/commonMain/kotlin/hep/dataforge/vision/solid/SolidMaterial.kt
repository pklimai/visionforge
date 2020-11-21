package hep.dataforge.vision.solid

import hep.dataforge.meta.*
import hep.dataforge.meta.descriptors.NodeDescriptor
import hep.dataforge.meta.descriptors.attributes
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.names.plus
import hep.dataforge.values.ValueType
import hep.dataforge.values.asValue
import hep.dataforge.vision.Colors
import hep.dataforge.vision.setProperty
import hep.dataforge.vision.solid.SolidMaterial.Companion.MATERIAL_COLOR_KEY
import hep.dataforge.vision.solid.SolidMaterial.Companion.MATERIAL_KEY
import hep.dataforge.vision.solid.SolidMaterial.Companion.MATERIAL_OPACITY_KEY
import hep.dataforge.vision.widgetType

public class SolidMaterial : Scheme() {

    /**
     * Primary web-color for the material
     */
    public var color: String? by string(key = COLOR_KEY)

    /**
     * Specular color for phong material
     */
    public var specularColor: String? by string(key = SPECULAR_COLOR_KEY)

    /**
     * Opacity
     */
    public var opacity: Float by float(1f, key = OPACITY_KEY)

    /**
     * Replace material by wire frame
     */
    public var wireframe: Boolean by boolean(false, WIREFRAME_KEY)

    public companion object : SchemeSpec<SolidMaterial>(::SolidMaterial) {

        public val MATERIAL_KEY: Name = "material".asName()
        public val COLOR_KEY: Name = "color".asName()
        public val MATERIAL_COLOR_KEY: Name = MATERIAL_KEY + COLOR_KEY
        public val SPECULAR_COLOR_KEY: Name = "specularColor".asName()
        public val MATERIAL_SPECULAR_COLOR_KEY: Name = MATERIAL_KEY + SPECULAR_COLOR_KEY
        public val OPACITY_KEY: Name = "opacity".asName()
        public val MATERIAL_OPACITY_KEY: Name = MATERIAL_KEY + OPACITY_KEY
        public val WIREFRAME_KEY: Name = "wireframe".asName()
        public val MATERIAL_WIREFRAME_KEY: Name = MATERIAL_KEY + WIREFRAME_KEY

        public override val descriptor: NodeDescriptor by lazy {
            //must be lazy to avoid initialization bug
            NodeDescriptor {
                value(COLOR_KEY) {
                    type(ValueType.STRING, ValueType.NUMBER)
                    widgetType = "color"
                }
                value(OPACITY_KEY) {
                    type(ValueType.NUMBER)
                    default(1.0)
                    attributes {
                        this["min"] = 0.0
                        this["max"] = 1.0
                        this["step"] = 0.1
                    }
                    widgetType = "slider"
                }
                value(WIREFRAME_KEY) {
                    type(ValueType.BOOLEAN)
                    default(false)
                }
            }
        }
    }
}

/**
 * Set color as web-color
 */
public fun Solid.color(webColor: String) {
    setProperty(MATERIAL_COLOR_KEY, webColor.asValue())
}

/**
 * Set color as integer
 */
public fun Solid.color(rgb: Int) {
    setProperty(MATERIAL_COLOR_KEY, rgb.asValue())
}

public fun Solid.color(r: UByte, g: UByte, b: UByte): Unit = setProperty(
    MATERIAL_COLOR_KEY,
    Colors.rgbToString(r, g, b).asValue()
)

/**
 * Web colors representation of the color in `#rrggbb` format or HTML name
 */
public var Solid.color: String?
    get() = getProperty(MATERIAL_COLOR_KEY)?.let { Colors.fromMeta(it) }
    set(value) {
        setProperty(MATERIAL_COLOR_KEY, value?.asValue())
    }

public val Solid.material: SolidMaterial?
    get() = getProperty(MATERIAL_KEY).node?.let { SolidMaterial.read(it) }

public fun Solid.material(builder: SolidMaterial.() -> Unit) {
    val node = config[MATERIAL_KEY].node
    if (node != null) {
        SolidMaterial.update(node, builder)
    } else {
        config[MATERIAL_KEY] = SolidMaterial(builder)
    }
}

public var Solid.opacity: Double?
    get() = getProperty(MATERIAL_OPACITY_KEY).double
    set(value) {
        setProperty(MATERIAL_OPACITY_KEY, value?.asValue())
    }