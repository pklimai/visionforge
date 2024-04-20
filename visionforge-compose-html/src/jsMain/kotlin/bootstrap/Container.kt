package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
public fun Container(
    fluid: Boolean = false,
    type: Breakpoint? = null,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>?
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Div(attrs = {
        if (classes != null) {
            classes(classes = classes)
        }
        when {
            fluid -> {
                classes("container-fluid")
            }
            type != null -> {
                classes("container-$type")
            }
            else -> {
                classes("container")
            }
        }

        attrs?.invoke(this)
    }, content)
}
