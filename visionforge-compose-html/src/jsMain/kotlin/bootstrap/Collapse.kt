package bootstrap

import androidx.compose.runtime.Composable
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement

@Composable
public fun Collapse(
    title: String,
    id: String = uuid4().toString(),
    color: Color = Color.Primary,
    buttonAttrs: AttrBuilderContext<HTMLButtonElement>? = null,
    contentAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>
) {
    Style
    needsJS
    Button(attrs = {
        classes("btn", "btn-$color")
        attr("data-bs-toggle", "collapse")
        attr("data-bs-target", "#_$id")
        attr("aria-expanded", "false")
        attr("aria-controls", "_$id")
        buttonAttrs?.invoke(this)
    }, type = ButtonType.Button, title = title) { }
    Div({
        classes("collapse")
        id("_$id")
        contentAttrs?.invoke(this)
    }, content)
}
