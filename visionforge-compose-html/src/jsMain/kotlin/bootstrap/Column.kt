package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
public fun Column(
    auto: Boolean = false,
    breakpoint: Breakpoint? = null,
    size: Int? = null,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Div(attrs = {
        classes("col" + (breakpoint?.let { "-$it" } ?: "") + (size?.let { "-$it" } ?: ""))
        if (classes != null) {
            classes(classes = classes)
        }
        if (auto) {
            classes("col-auto")
        }
        attrs?.invoke(this)
    }, content)
}
