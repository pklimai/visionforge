package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
public fun Card(
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    headerAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    header: ContentBuilder<HTMLDivElement>? = null,
    footerAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    footer: ContentBuilder<HTMLDivElement>? = null,
    bodyAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    body: ContentBuilder<HTMLDivElement>
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Div({
        classes("card")
        if (classes != null) {
            classes(classes = classes)
        }
        attrs?.invoke(this)
    }) {
        header?.let {
            Div({
                classes("card-header")
                headerAttrs?.invoke(this)
            }, header)
        }
        Div({
            classes("card-body")
            bodyAttrs?.invoke(this)
        }, body)
        footer?.let {
            Div({
                classes("card-footer")
                footerAttrs?.invoke(this)
            }, footer)
        }
    }
}
