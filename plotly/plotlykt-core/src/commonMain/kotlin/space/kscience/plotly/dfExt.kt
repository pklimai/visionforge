package space.kscience.plotly

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.dataforge.names.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

//extensions for DataForge


/**
 * List of values delegate
 */
internal fun Scheme.listOfValues(
    key: Name? = null,
): ReadWriteProperty<Any?, List<Value>> = object : ReadWriteProperty<Any?, List<Value>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<Value> {
        val name = key ?: property.name.asName()
        return meta[name]?.value?.list ?: emptyList()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Value>) {
        val name = key ?: property.name.asName()
        meta[name] = value.asValue()
    }
}

/**
 * A safe [Double] range
 */
internal fun Scheme.doubleInRange(
    range: ClosedFloatingPointRange<Double>,
    defaultValue: Double = Double.NaN,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return meta[name].double ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Double] ray
 */
internal fun Scheme.doubleGreaterThan(
    minValue: Double,
    key: Name? = null,
): ReadWriteProperty<Any?, Double> = object : ReadWriteProperty<Any?, Double> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        val name = key ?: property.name.asName()
        return meta[name].double ?: Double.NaN
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}


/**
 * A safe [Int] ray
 */
internal fun Scheme.intGreaterThan(
    minValue: Int,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return meta[name].int ?: minValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value >= minValue) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Int] range
 */
internal fun Scheme.intInRange(
    range: ClosedRange<Int>,
    key: Name? = null,
): ReadWriteProperty<Any?, Int> = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = key ?: property.name.asName()
        return meta[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = key ?: property.name.asName()
        if (value in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

/**
 * A safe [Number] ray
 */
public fun Scheme.numberGreaterThan(
    minValue: Number,
    default: Number = minValue,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return meta[name].number ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() >= minValue.toDouble()) {
            meta[name] = value
        } else {
            error("$value less than $minValue")
        }
    }
}

/**
 * A safe [Number] range
 */
public fun Scheme.numberInRange(
    range: ClosedRange<Double>,
    key: Name? = null,
): ReadWriteProperty<Any?, Number> = object : ReadWriteProperty<Any?, Number> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Number {
        val name = key ?: property.name.asName()
        return meta[name].int ?: 0
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Number) {
        val name = key ?: property.name.asName()
        if (value.toDouble() in range) {
            meta[name] = value
        } else {
            error("$value not in range $range")
        }
    }
}

internal fun Scheme.duration(
    default: Duration? = null,
    key: Name? = null,
): ReadWriteProperty<Any?, Duration?> = object : ReadWriteProperty<Any?, Duration?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Duration? {
        val name = key ?: property.name.asName()
        val value = meta[name]?.value
        val units = meta[name + "unit"]?.enum() ?: DurationUnit.MILLISECONDS
        return value?.long?.toDuration(units) ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration?) {
        val name = key ?: property.name.asName()
        if (value == null) {
            meta.remove(name)
        } else {
            meta.value = value.toDouble(DurationUnit.MILLISECONDS).asValue()
            meta[name + "unit"] = DurationUnit.MILLISECONDS.name
        }
    }
}

/**
 * Append the observable note to same-name-siblings and observe its changes.
 */
@OptIn(DFExperimental::class)
internal fun ObservableMutableMeta.appendAndAttach(key: String, meta: ObservableMutableMeta) {
    val name = Name.parse(key)
    require(!name.isEmpty()) { "Name could not be empty for append operation" }
    val newIndex = name.lastOrNull()!!.index
    if (newIndex != null) {
        attach(name, meta)
    } else {
        val index = (getIndexed(name).keys.mapNotNull { it?.toIntOrNull() }.maxOrNull() ?: -1) + 1
        attach(name.withIndex(index.toString()), meta)
    }
}