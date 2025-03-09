package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
public fun ButtonGroup(
    styling: (Styling.() -> Unit)? = null,
    content: ContentBuilder<HTMLDivElement>
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Div({
        classes("btn-group")
        if (classes != null) {
            classes(classes = classes)
        }
    }, content)
}
