package bootstrap

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Label
import org.w3c.dom.HTMLLabelElement

@Composable
public fun FormLabel(
    forId: String? = null,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLLabelElement>? = null,
    content: ContentBuilder<HTMLLabelElement>? = null
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Label(
        forId = forId,
        attrs = {
            classes(BSClasses.formLabel)
            if (classes != null) {
                classes(classes = classes)
            }
            attrs?.invoke(this)
        },
        content = content
    )
}
