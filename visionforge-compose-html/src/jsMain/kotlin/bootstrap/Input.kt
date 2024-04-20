package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.autoComplete
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.events.SyntheticInputEvent
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Text

@Composable
public fun <T> Input(
    label: String,
    value: String,
    type: InputType<T>,
    placeholder: String? = null,
    autocomplete: AutoComplete = AutoComplete.off,
    labelClasses: String = "form-label",
    inputClasses: String = "form-control",
    attrs: (InputAttrsScope<T>.() -> Unit)? = null,
    onInput: (SyntheticInputEvent<T, HTMLInputElement>) -> Unit
) {
    Style
    Label(forId = null, attrs = {
        classes(labelClasses)
    }) {
        Text(label)
        Input(type = type, attrs = {
            autoComplete(autocomplete)
            attrs?.invoke(this)
            classes(inputClasses)
            value(value)
            if (placeholder != null) {
                placeholder(placeholder)
            }
            onInput {
                onInput(it)
            }
        })
    }
}
