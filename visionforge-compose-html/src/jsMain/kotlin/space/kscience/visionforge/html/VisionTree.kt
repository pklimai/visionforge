package space.kscience.visionforge.html

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import space.kscience.dataforge.names.*
import space.kscience.visionforge.Vision
import space.kscience.visionforge.VisionGroup


@Composable
private fun TreeLabel(
    vision: Vision,
    name: Name,
    selected: Name?,
    clickCallback: (Name) -> Unit,
) {
    Span({
        classes(TreeStyles.treeLabel)
        if (name == selected) {
            classes(TreeStyles.treeLabelSelected)
        }
        style {
            color(Color("#069"))
            cursor("pointer")
        }
        onClick { clickCallback(name) }
    }) {
        Text(name.lastOrNull()?.toString() ?: "World")
    }
}

@Composable
public fun VisionTree(
    vision: Vision,
    name: Name = Name.EMPTY,
    selected: Name? = null,
    clickCallback: (Name) -> Unit,
): Unit {
    var expanded: Boolean by remember { mutableStateOf(selected?.startsWith(name) ?: false) }

    //display as node if any child is visible
    if (vision is VisionGroup<*>) {
        FlexRow {
            if (vision.items.keys.any { !it.first().body.startsWith("@") }) {
                Span({
                    classes(TreeStyles.treeCaret)
                    if (expanded) {
                        classes(TreeStyles.treeCaretDown)
                    }
                    onClick {
                        expanded = !expanded
                    }
                })
            }
            TreeLabel(vision, name, selected, clickCallback)
        }
        if (expanded) {
            FlexColumn({
                classes(TreeStyles.tree)
            }) {
                vision.items.asSequence()
                    .filter { !it.key.toString().startsWith("@") } // ignore statics and other hidden children
                    .sortedBy { (it.value as? VisionGroup<Vision>)?.items?.isEmpty() ?: true } // ignore empty groups
                    .forEach { (childToken, child) ->
                        Div({ classes(TreeStyles.treeItem) }) {
                            VisionTree(
                                child,
                                name + childToken,
                                selected,
                                clickCallback
                            )
                        }
                    }
            }
        }
    } else {
        TreeLabel(vision, name, selected, clickCallback)
    }
}
