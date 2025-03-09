package bootstrap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.forId
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
public fun Checkbox(
    checked: Boolean,
    label: String,
    id: String = remember { uuid4().toString() },
    disabled: Boolean = false,
    inline: Boolean = false,
    switch: Boolean = false,
    styling: (Styling.() -> Unit)? = null,
    attrs: (InputAttrsScope<Boolean>.() -> Unit)? = null,
    onClick: (Boolean) -> Unit
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Div({
        classes(BSClasses.formCheck)
        if (classes != null) {
            classes(classes = classes)
        }
        if (inline) {
            classes(BSClasses.formCheckInline)
        }
        if (switch) {
            classes(BSClasses.formSwitch)
        }
    }) {
        Input(attrs = {
            classes(BSClasses.formCheckInput)
            id("_$id")
            checked(checked)
            onInput { event ->
                onClick(event.value)
            }
            if (disabled) {
                disabled()
            }
            attrs?.invoke(this)
        }, type = InputType.Checkbox)
        Label(attrs = {
            classes(BSClasses.formCheckLabel)
            forId("_$id")
        }) {
            Text(label)
        }
    }
}
