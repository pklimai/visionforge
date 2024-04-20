package bootstrap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
@NonRestartableComposable
public fun FormFloating(
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }
    Div(attrs = {
        classes("form-floating")
        if (classes != null) {
            classes(classes = classes)
        }
        if (attrs != null) {
            attrs()
        }
    }, content)
}
