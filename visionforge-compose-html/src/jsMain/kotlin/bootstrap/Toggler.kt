package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLButtonElement

@Composable
public fun Toggler(
    target: String,
    controls: String,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLButtonElement>? = null
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }
    Button(attrs = {
        classes("navbar-toggler")
        if (classes != null) {
            classes(classes = classes)
        }
        attr("data-bs-toggle", "collapse")
        attr("data-bs-target", "#$target")
        attr("aria-controls", controls)
        attr("aria-expanded", "false")
        attr("aria-label", "Toggle navigation")
        attrs?.invoke(this)
    }) {
        Span(attrs = { classes("navbar-toggler-icon") })
    }
}
