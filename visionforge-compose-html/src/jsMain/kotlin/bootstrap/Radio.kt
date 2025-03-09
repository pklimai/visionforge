package bootstrap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.forId
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
public fun RadioGroup(content: @Composable RadioGroupScope.() -> Unit) {
    val groupName = remember { "_${uuid4()}" }
    val context = RadioGroupScope(groupName)
    context.content()
}

public class RadioGroupScope(private val name: String) {
    @Composable
    public fun Radio(
        label: String,
        checked: Boolean = false,
        disabled: Boolean = false,
        inline: Boolean = false,
        styling: (Styling.() -> Unit)? = null,
        onClick: (Boolean) -> Unit,
    ) {
        val id = remember { "_${uuid4()}" }

        Style
        val classes = styling?.let {
            Styling().apply(it).generate()
        }

        Div(attrs = {
            classes(BSClasses.formCheck)
            if (inline) {
                classes(BSClasses.formCheckInline)
            }
            if (classes != null) {
                classes(classes = classes)
            }
        }) {
            Input(attrs = {
                classes(BSClasses.formCheckInput)
                id(id)
                checked(checked)

                onInput { event ->
                    onClick(event.value)
                }
                if (disabled) {
                    disabled()
                }
                name(name)
            }, type = InputType.Radio)
            Label(attrs = {
                classes(BSClasses.formCheckLabel)
                forId(id)
            }) {
                Text(label)
            }
        }
    }
}
