package space.kscience.visionforge.solid.three.compose

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.Card
import app.softwork.bootstrapcompose.Column
import app.softwork.bootstrapcompose.Layout
import app.softwork.bootstrapcompose.Row
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.dom.clear
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.request
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.isEmpty
import space.kscience.visionforge.VisionPropertyChangedEvent
import space.kscience.visionforge.html.*
import space.kscience.visionforge.solid.Solid
import space.kscience.visionforge.solid.SolidGroup
import space.kscience.visionforge.solid.get
import space.kscience.visionforge.solid.specifications.Canvas3DOptions
import space.kscience.visionforge.solid.three.ThreeCanvas
import space.kscience.visionforge.solid.three.ThreePlugin
import space.kscience.visionforge.styles
import space.kscience.visionforge.writeProperties

@Composable
private fun SimpleThreeView(
    context: Context,
    options: Canvas3DOptions?,
    solid: Solid?,
    selected: Name?,
) {

    Div({
        style {
            maxWidth(100.vw)
            maxHeight(100.vh)
            width(100.percent)
            height(100.percent)
        }
    }) {
        var canvas: ThreeCanvas? by remember { mutableStateOf(null) }
        DisposableEffect(options) {
            canvas = ThreeCanvas(context.request(ThreePlugin), scopeElement, options ?: Canvas3DOptions())
            onDispose {
                scopeElement.clear()
                canvas = null
            }
        }
        LaunchedEffect(solid) {
            if (solid != null) {
                canvas?.render(solid)
            } else {
                canvas?.clear()
            }
        }
        LaunchedEffect(selected) {
            canvas?.select(selected)
        }
    }
}


@Composable
public fun ThreeView(
    context: Context,
    solid: Solid?,
    initialSelected: Name? = null,
    options: Canvas3DOptions? = null,
    sidebarTabs: @Composable TabsBuilder.() -> Unit = {},
) {
    var selected: Name? by remember { mutableStateOf(initialSelected) }

    val optionsSnapshot by derivedStateOf {
        (options ?: Canvas3DOptions()).apply {
            this.onSelect = {
                selected = it
            }
        }
    }

    if (optionsSnapshot.controls.enabled) {

        Row(
            styling = {
                Layout {
                    width = Layout.Width.Full
                    height = Layout.Height.Full
                }
            }
        ) {
            Column(
                styling = {
                    Layout {
                        height = Layout.Height.Full
                    }
                },
                attrs = {
                    style {
                        position(Position.Relative)
                        minWidth(600.px)
                    }
                }
            ) {
                if (solid == null) {
                    Div({ classes("d-flex", "justify-content-center") }) {
                        Div({
                            classes("spinner-border")
                            attr("role", "status")
                        }) {
                            Span({ classes("visually-hidden") }) {
                                Text("Loading...")
                            }
                        }
                    }
                } else {
                    SimpleThreeView(context, optionsSnapshot, solid, selected)
                }

                key(selected) {
                    selected?.let {
                        when {
                            it.isEmpty() -> solid
                            else -> (solid as? SolidGroup)?.get(it)
                        }
                    }?.let { vision ->
                        Card(attrs = {
                            style {
                                position(Position.Absolute)
                                top(10.px)
                                right(10.px)
                                width(450.px)
                                overflowY("auto")
                            }
                        }) {
                            NameCrumbs(selected) { selected = it }
                            Hr()
                            PropertyEditor(
                                rootMeta = vision.writeProperties(),
                                getPropertyState = { name ->
                                    if (vision.properties[name] != null) {
                                        EditorPropertyState.Defined
                                    } else if (vision.properties[name] != null) {
                                        // TODO differentiate
                                        EditorPropertyState.Default()
                                    } else {
                                        EditorPropertyState.Undefined
                                    }
                                },
                                name = Name.EMPTY,
                                updates = vision.eventFlow
                                    .filterIsInstance<VisionPropertyChangedEvent>()
                                    .map { it.propertyName },
                                rootDescriptor = vision.descriptor
                            )
                            vision.styles.takeIf { it.isNotEmpty() }?.let { styles ->
                                Hr()
                                P {
                                    B { Text("Styles: ") }
                                    Text(styles.joinToString(separator = ", "))
                                }
                            }
                        }
                    }
                }
            }

            Column(
                auto = true,
                styling = {
                    Layout {
                        height = Layout.Height.Full
                    }
                },
                attrs = {
                    style {
                        paddingAll(4.px)
                        minWidth(400.px)
                        height(100.percent)
                    }
                }
            ) {
                ThreeControls(
                    solid,
                    optionsSnapshot,
                    selected,
                    onSelect = { selected = it },
                    tabBuilder = sidebarTabs
                )
            }
        }
    } else {
        SimpleThreeView(context, optionsSnapshot, solid, selected)
    }
}