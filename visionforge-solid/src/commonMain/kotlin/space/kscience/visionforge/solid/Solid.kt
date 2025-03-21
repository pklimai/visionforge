package space.kscience.visionforge.solid

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.enum
import space.kscience.dataforge.meta.descriptors.node
import space.kscience.dataforge.meta.descriptors.value
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.asName
import space.kscience.dataforge.names.plus
import space.kscience.kmath.complex.Quaternion
import space.kscience.kmath.complex.QuaternionAlgebra
import space.kscience.kmath.geometry.Angle
import space.kscience.kmath.geometry.euclidean3d.*
import space.kscience.kmath.geometry.radians
import space.kscience.visionforge.MutableVision
import space.kscience.visionforge.Vision
import space.kscience.visionforge.Vision.Companion.VISIBLE_KEY
import space.kscience.visionforge.hide
import space.kscience.visionforge.inherited
import space.kscience.visionforge.solid.Solid.Companion.DETAIL_KEY
import space.kscience.visionforge.solid.Solid.Companion.IGNORE_KEY
import space.kscience.visionforge.solid.Solid.Companion.LAYER_KEY
import space.kscience.visionforge.solid.Solid.Companion.POSITION_KEY
import space.kscience.visionforge.solid.Solid.Companion.ROTATION_KEY
import space.kscience.visionforge.solid.Solid.Companion.SCALE_KEY
import space.kscience.visionforge.solid.Solid.Companion.X_KEY
import space.kscience.visionforge.solid.Solid.Companion.X_POSITION_KEY
import space.kscience.visionforge.solid.Solid.Companion.X_ROTATION_KEY
import space.kscience.visionforge.solid.Solid.Companion.X_SCALE_KEY
import space.kscience.visionforge.solid.Solid.Companion.Y_KEY
import space.kscience.visionforge.solid.Solid.Companion.Y_POSITION_KEY
import space.kscience.visionforge.solid.Solid.Companion.Y_ROTATION_KEY
import space.kscience.visionforge.solid.Solid.Companion.Y_SCALE_KEY
import space.kscience.visionforge.solid.Solid.Companion.Z_KEY
import space.kscience.visionforge.solid.Solid.Companion.Z_POSITION_KEY
import space.kscience.visionforge.solid.Solid.Companion.Z_ROTATION_KEY
import space.kscience.visionforge.solid.Solid.Companion.Z_SCALE_KEY
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Interface for a [Vision] representing a 3D object
 */
public interface Solid : MutableVision {

    override val descriptor: MetaDescriptor get() = Companion.descriptor

    public companion object {
        //        val SELECTED_KEY = "selected".asName()
        public val DETAIL_KEY: Name = "detail".asName()
        public val LAYER_KEY: Name = "layer".asName()
        public val IGNORE_KEY: Name = "ignore".asName()

        public val GEOMETRY_KEY: Name = "geometry".asName()

        public val X_KEY: Name = "x".asName()
        public val Y_KEY: Name = "y".asName()
        public val Z_KEY: Name = "z".asName()

        public val POSITION_KEY: Name = "position".asName()

        public val X_POSITION_KEY: Name = POSITION_KEY + X_KEY
        public val Y_POSITION_KEY: Name = POSITION_KEY + Y_KEY
        public val Z_POSITION_KEY: Name = POSITION_KEY + Z_KEY

        public val ROTATION_KEY: Name = "rotation".asName()

        public val X_ROTATION_KEY: Name = ROTATION_KEY + X_KEY
        public val Y_ROTATION_KEY: Name = ROTATION_KEY + Y_KEY
        public val Z_ROTATION_KEY: Name = ROTATION_KEY + Z_KEY

        public val ROTATION_ORDER_KEY: Name = ROTATION_KEY + "order"

        public val SCALE_KEY: Name = "scale".asName()

        public val X_SCALE_KEY: Name = SCALE_KEY + X_KEY
        public val Y_SCALE_KEY: Name = SCALE_KEY + Y_KEY
        public val Z_SCALE_KEY: Name = SCALE_KEY + Z_KEY

        public val descriptor: MetaDescriptor by lazy {
            MetaDescriptor {
                value(VISIBLE_KEY, ValueType.BOOLEAN) {
                    inherited = false
                    default(true)
                }

                node(SolidMaterial.MATERIAL_KEY.toString(), SolidMaterial)

                //TODO replace by descriptor merge
                value(Vision.STYLE_KEY, ValueType.STRING) {
                    multiple = true
                    hide()
                }

                node(POSITION_KEY) {
                    hide()
                }

                node(ROTATION_KEY) {
                    hide()
                }

                node(SCALE_KEY) {
                    hide()
                }

                value(DETAIL_KEY, ValueType.NUMBER) {
                    hide()
                }

                enum(ROTATION_ORDER_KEY, default = RotationOrder.XYZ) {
                    hide()
                }
            }
        }
    }
}

/**
 * Get the layer number this solid belongs to. Return 0 if layer is not defined.
 */
public var Solid.layer: Int
    get() = readProperty(LAYER_KEY, inherited = true).int ?: 0
    set(value) {
        properties[LAYER_KEY] = value
    }

// Common properties

/**
 * Rotation order
 */
public var Solid.rotationOrder: RotationOrder
    get() = properties.getValue(Solid.ROTATION_ORDER_KEY)?.enum<RotationOrder>() ?: RotationOrder.XYZ
    set(value) = properties.setValue(Solid.ROTATION_ORDER_KEY, value.name.asValue())


/**
 * Preferred number of polygons for displaying the object. If not defined, uses shape or renderer default. Not inherited
 */
public var Solid.detail: Int?
    get() = readProperty(DETAIL_KEY, inherited = false).int
    set(value) = properties.setValue(DETAIL_KEY, value?.asValue())

/**
 * If this property is true, the object will be ignored on render.
 * Property is not inherited.
 */
public var MutableVision.ignore: Boolean?
    get() = readProperty(IGNORE_KEY, inherited = false, useStyles = false).boolean
    set(value) = properties.setValue(IGNORE_KEY, value?.asValue())

//var VisualObject.selected: Boolean?
//    get() = getProperty(SELECTED_KEY).boolean
//    set(value) = setProperty(SELECTED_KEY, value)

/**
 *A [Float] solid property delegate
 */
internal fun float32(name: Name, default: Number): ReadWriteProperty<Solid, Number> =
    object : ReadWriteProperty<Solid, Number> {
        override fun getValue(thisRef: Solid, property: KProperty<*>): Number {
            return thisRef.properties.getValue(name)?.number ?: default
        }

        override fun setValue(thisRef: Solid, property: KProperty<*>, value: Number) {
            thisRef.properties.setValue(name, value.asValue())
        }
    }

/**
 * A [FloatVector3D] solid property delegate
 */
internal fun float32Vector(
    name: Name,
    defaultX: Float,
    defaultY: Float = defaultX,
    defaultZ: Float = defaultX,
): ReadWriteProperty<Solid, FloatVector3D?> =
    object : ReadWriteProperty<Solid, FloatVector3D?> {
        override fun getValue(thisRef: Solid, property: KProperty<*>): FloatVector3D? {
            val item = thisRef.properties[name] ?: return null
            //using dynamic property accessor because values could change
            return object : FloatVector3D {
                override val x: Float get() = item[X_KEY]?.float ?: defaultX
                override val y: Float get() = item[Y_KEY]?.float ?: defaultY
                override val z: Float get() = item[Z_KEY]?.float ?: defaultZ

                override fun toString(): String = item.toString()
            }
        }

        override fun setValue(thisRef: Solid, property: KProperty<*>, value: FloatVector3D?) {
            if (value == null) {
                thisRef.properties[name] = null
            } else {
                thisRef.properties[name + X_KEY] = value.x
                thisRef.properties[name + Y_KEY] = value.y
                thisRef.properties[name + Z_KEY] = value.z
            }
        }
    }

public var Solid.position: FloatVector3D? by float32Vector(POSITION_KEY, 0f)
public var Solid.rotation: FloatVector3D? by float32Vector(ROTATION_KEY, 0f)
public var Solid.scale: FloatVector3D? by float32Vector(SCALE_KEY, 1f)

public fun Solid.scale(scaleFactor: Number) {
    scale = Float32Vector3D(scaleFactor, scaleFactor, scaleFactor)
}

public var Solid.x: Number by float32(X_POSITION_KEY, 0f)
public var Solid.y: Number by float32(Y_POSITION_KEY, 0f)
public var Solid.z: Number by float32(Z_POSITION_KEY, 0f)

public var Solid.rotationX: Number by float32(X_ROTATION_KEY, 0f)
public var Solid.rotationY: Number by float32(Y_ROTATION_KEY, 0f)
public var Solid.rotationZ: Number by float32(Z_ROTATION_KEY, 0f)

/**
 * Raw quaternion value defined in properties
 */
public var Solid.quaternionOrNull: Quaternion?
    get() = properties.getValue(ROTATION_KEY)?.list?.let {
        require(it.size == 4) { "Quaternion must be a number array of 4 elements" }
        Quaternion(it[0].float, it[1].float, it[2].float, it[3].float)
    }
    set(value) {
        properties.setValue(
            ROTATION_KEY,
            value?.let {
                ListValue(
                    value.w,
                    value.x,
                    value.y,
                    value.z
                )
            }
        )
    }

/**
 * Quaternion value including information from euler angles
 */
public var Solid.quaternion: Quaternion
    get() = quaternionOrNull ?: Quaternion.fromEuler(
        rotationX.radians,
        rotationY.radians,
        rotationZ.radians,
        rotationOrder
    )
    set(value) {
        quaternionOrNull = value
    }

public var Solid.scaleX: Number by float32(X_SCALE_KEY, 1f)
public var Solid.scaleY: Number by float32(Y_SCALE_KEY, 1f)
public var Solid.scaleZ: Number by float32(Z_SCALE_KEY, 1f)

/**
 * Add rotation with given [angle] relative to given [axis]
 */
public fun Solid.rotate(angle: Angle, axis: Float64Vector3D): Unit = with(QuaternionAlgebra) {
    quaternion = Quaternion.fromRotation(angle, axis) * quaternion
}