package bootstrap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Select
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement

@Composable
public fun Select(
    multiple: Boolean = false,
    size: SelectSize = SelectSize.Default,
    rows: Int? = null,
    disabled: Boolean = false,
    autocomplete: AutoComplete = AutoComplete.off,
    id: String = remember { "_${uuid4()}" },
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLSelectElement>? = null,
    onChange: (List<String>) -> Unit,
    content: @Composable SelectContext.() -> Unit,
) {
    Style
    val classes = styling?.let {
        Styling().apply(it).generate()
    }

    Select(attrs = {
        id(id)
        classes(BSClasses.formSelect)
        if (classes != null) {
            classes(classes = classes)
        }
        if (multiple) {
            multiple()
        }
        when (size) {
            SelectSize.Small -> classes(BSClasses.formSelectSmall)
            SelectSize.Large -> classes(BSClasses.formSelectLarge)
            else -> {
            }
        }
        rows?.let {
            size(it)
        }
        if (disabled) {
            disabled()
        }
        autoComplete(autocomplete)
        attrs?.invoke(this)
        onChange { event ->
            val selectElement = event.nativeEvent.target as HTMLSelectElement
            val options = selectElement.selectedOptions
            val results: MutableList<String> = mutableListOf()
            for (i in 0..options.length) {
                options.item(i)?.let {
                    results += (it as HTMLOptionElement).value
                }
            }
            onChange(results)
        }
    }) {
        val scope = SelectContext()
        scope.content()
    }
}

public class SelectContext {
    @Composable
    public fun Option(
        value: String,
        selected: Boolean? = null,
        styling: (Styling.() -> Unit)? = null,
        attrs: AttrBuilderContext<HTMLOptionElement>? = null,
        content: ContentBuilder<HTMLOptionElement>? = null,
    ) {
        Style
        val classes = styling?.let {
            Styling().apply(it).generate()
        }

        org.jetbrains.compose.web.dom.Option(
            value = value,
            attrs = {
                if (selected == true) {
                    selected()
                }
                if (classes != null) {
                    classes(classes = classes)
                }
                attrs?.invoke(this)
            },
            content = content
        )
    }
}

public enum class SelectSize {
    Small,
    Default,
    Large
}
