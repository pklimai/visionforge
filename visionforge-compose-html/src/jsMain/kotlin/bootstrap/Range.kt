package bootstrap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.RangeInput
import org.jetbrains.compose.web.events.SyntheticInputEvent
import org.w3c.dom.HTMLInputElement

@Composable
public fun Range(
    value: Number,
    min: Number? = null,
    max: Number? = null,
    step: Number = 1,
    disabled: Boolean = false,
    id: String = remember { "_${uuid4()}" },
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLInputElement>? = null,
    onInput: (SyntheticInputEvent<Number?, HTMLInputElement>) -> Unit,
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    RangeInput(value, min, max, step) {
        if (classes != null) {
            classes(classes = classes)
        }
        onInput {
            onInput(it)
        }
        if (disabled) {
            disabled()
        }
        id(id)
        classes(BSClasses.formRange)
        attrs?.invoke(this)
    }
}
