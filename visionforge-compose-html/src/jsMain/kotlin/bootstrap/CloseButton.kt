package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.dom.Button

@Composable
public fun CloseButton(
    disabled: Boolean = false,
    onClose: () -> Unit
) {
    Style
    Button({
        type(ButtonType.Button)
        classes("btn-close")
        attr("aria-label", "Close")
        if (disabled) {
            disabled()
        }
        onClick {
            onClose()
        }
    })
}
