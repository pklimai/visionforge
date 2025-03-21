package space.kscience.visionforge.html

import androidx.compose.runtime.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import space.kscience.dataforge.meta.MutableMeta
import space.kscience.dataforge.meta.ObservableMutableMeta
import space.kscience.dataforge.meta.descriptors.MetaDescriptor
import space.kscience.dataforge.meta.descriptors.ValueRestriction
import space.kscience.dataforge.meta.descriptors.get
import space.kscience.dataforge.meta.remove
import space.kscience.dataforge.names.*
import space.kscience.visionforge.hidden


/**
 * The display state of a property
 */
public sealed class EditorPropertyState {
    public data object Defined : EditorPropertyState()
    public data class Default(public val source: String = "unknown") : EditorPropertyState()
    public data object Undefined : EditorPropertyState()
}

/**
 * @param rootMeta Root config object - always non-null
 * @param rootDescriptor Full path to the displayed node in [rootMeta]. Could be empty
 */
@Composable
public fun PropertyEditor(
    rootMeta: MutableMeta,
    getPropertyState: (Name) -> EditorPropertyState,
    updates: Flow<Name>,
    name: Name,
    rootDescriptor: MetaDescriptor?,
    initialExpanded: Boolean? = null,
) {
    var expanded: Boolean by remember { mutableStateOf(initialExpanded ?: true) }
    val descriptor: MetaDescriptor? by derivedStateOf { rootDescriptor?.get(name) }
    var displayedValue by remember { mutableStateOf(rootMeta.getValue(name)) }
    var editorPropertyState: EditorPropertyState by remember { mutableStateOf(getPropertyState(name)) }


    val keys by derivedStateOf {
        buildSet {
            descriptor?.nodes?.filterNot {
                it.key.startsWith("@") || it.value.hidden
            }?.forEach {
                add(NameToken(it.key))
            }
            //ownProperty?.items?.keys?.filterNot { it.body.startsWith("@") }?.let { addAll(it) }
        }
    }

    val token = name.lastOrNull()?.toString() ?: "Properties"

    fun update() {
        displayedValue = rootMeta.getValue(name)
        editorPropertyState = getPropertyState(name)
    }

    LaunchedEffect(rootMeta) {
        updates.collect { updatedName ->
            if (name.startsWith(updatedName)) {
                update()
            }
        }
    }

    FlexRow({
        style {
            alignItems(AlignItems.Center)
        }
    }) {
        //if node has children
        if (keys.isNotEmpty()) {
            Span({
                classes(TreeStyles.treeCaret)
                if (expanded) {
                    classes(TreeStyles.treeCaretDown)
                }
                onClick { expanded = !expanded }
            })
        }
        Span({
            classes(TreeStyles.treeLabel)
            when (editorPropertyState) {
                is EditorPropertyState.Default, EditorPropertyState.Undefined -> {
                    classes(TreeStyles.treeLabelInactive)
                }

                EditorPropertyState.Defined -> {}
            }
        }) {
            Text(token)
        }

        if (!name.isEmpty() && descriptor?.valueRestriction != ValueRestriction.ABSENT) {
            Div({
                style {
                    width(160.px)
                    marginAll(1.px, 5.px)
                }
            }) {
                ValueChooser(descriptor, editorPropertyState, displayedValue) {
                    rootMeta.setValue(name, it)
                    update()
                }
            }

        }
        if (!name.isEmpty()) {
            Button(attrs = {
                type(ButtonType.Button)
                if(editorPropertyState != EditorPropertyState.Defined) disabled()
                classes("btn-close")
                onClick {
                    rootMeta.remove(name)
                    update()

                }
            })
        }
    }
    if (expanded) {
        FlexColumn({
            classes(TreeStyles.tree)
        }) {
            keys.forEach { token ->
                Div({
                    classes(TreeStyles.treeItem)
                }) {
                    PropertyEditor(rootMeta, getPropertyState, updates, name + token, rootDescriptor, expanded)
                }
            }
        }
    }
}

@Composable
public fun PropertyEditor(
    properties: ObservableMutableMeta,
    descriptor: MetaDescriptor? = null,
    expanded: Boolean? = null,
) {
    val scope = rememberCoroutineScope()
    PropertyEditor(
        rootMeta = properties,
        getPropertyState = { name ->
            if (properties[name] != null) {
                EditorPropertyState.Defined
            } else if (descriptor?.get(name)?.defaultValue != null) {
                EditorPropertyState.Default("descriptor")
            } else {
                EditorPropertyState.Undefined
            }
        },
        updates = callbackFlow {
            properties.onChange(scope) { name ->
                scope.launch {
                    send(name)
                }
            }

            awaitClose { properties.removeListener(scope) }
        },
        name = Name.EMPTY,
        rootDescriptor = descriptor,
        initialExpanded = expanded,
    )
}