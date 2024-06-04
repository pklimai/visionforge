package space.kscience.plotly.models

import space.kscience.dataforge.meta.*
import space.kscience.dataforge.names.Name

/**
 * Type-safe accessor class for values in the trace
 */
public class TraceValues internal constructor(public val owner: Scheme, name: Name) {
    public var value: Value? by owner.value(key = name)

    public var doubles: DoubleArray
        get() = value?.doubleArray?.copyOf() ?: doubleArrayOf()
        set(value) {
            this.value = DoubleArrayValue(value)
        }

    public var numbers: Iterable<Number>
        get() = value?.list?.map { it.numberOrNull ?: Double.NaN } ?: emptyList()
        set(value) {
            this.value = ListValue(value.map { it.asValue() })
        }

    public var strings: Iterable<String>
        get() = value?.list?.map { it.string } ?: emptyList()
        set(value) {
            this.value = value.map { it.asValue() }.asValue()
        }

    /**
     * Smart fill for trace values. The following types are accepted: [DoubleArray], [IntArray], [Array] of primitive or string,
     * [Iterable] of primitive or string.
     */
    public fun set(values: Any?) {
        value = when (values) {
            null -> null
            is DoubleArray -> values.asValue()
            is IntArray -> values.map { it.asValue() }.asValue()
            is Array<*> -> values.map { Value.of(it) }.asValue()
            is Iterable<*> -> values.map { Value.of(it) }.asValue()
            else -> error("Unrecognized values type ${values::class}")
        }
    }

    public operator fun invoke(vararg numbers: Number) {
        this.numbers = numbers.asList()
    }

    public operator fun invoke(vararg strings: String) {
        this.strings = strings.asList()
    }

    public operator fun invoke(lists: List<List<Number>>) {
        this.value = lists.map { row -> row.map { it.asValue() }.asValue() }.asValue()
    }

}