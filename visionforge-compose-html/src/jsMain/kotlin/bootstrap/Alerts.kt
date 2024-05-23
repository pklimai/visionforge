package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement

@Composable
public fun Alert(
    color: Color,
    dismissible: Boolean = true,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable () -> Unit
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }
    Div({
        classes("alert", "alert-$color")
        if (dismissible) {
            needsJS
            classes("alert-dismissible")
        }
        if (classes != null) {
            classes(classes = classes)
        }
        attr("role", "alert")
        attrs?.invoke(this)
    }) {
        content()
    }
}

@Composable
public fun Link(
    href: String?,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    content: ContentBuilder<HTMLAnchorElement>
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }
    A(href, {
        classes("alert-link")
        if (classes != null) {
            classes(classes = classes)
        }
        attrs?.invoke(this)
    }, content)
}
