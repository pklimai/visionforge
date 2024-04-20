package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLSpanElement

@Composable
public fun Badge(
    backgroundColor: Color,
    textColor: Color? = null,
    round: Boolean = false,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLSpanElement>? = null,
    content: ContentBuilder<HTMLSpanElement>
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Span(attrs = {
        classes("badge", backgroundColor.background())
        if (textColor != null) {
            classes(textColor.text())
        }
        if (round) {
            classes("rounded-pill")
        }
        if (classes != null) {
            classes(classes = classes)
        }
        attrs?.invoke(this)
    }, content)
}
