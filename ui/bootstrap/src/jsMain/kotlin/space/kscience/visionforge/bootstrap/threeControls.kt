package space.kscience.visionforge.bootstrap

import kotlinx.css.*
import react.FC
import react.PropsWithChildren
import react.RBuilder
import react.dom.h2
import react.fc
import space.kscience.dataforge.names.Name
import space.kscience.dataforge.names.isEmpty
import space.kscience.visionforge.Vision
import space.kscience.visionforge.react.visionTree
import space.kscience.visionforge.solid.SolidGroup
import space.kscience.visionforge.solid.specifications.Canvas3DOptions
import styled.css
import styled.styledDiv

public external interface ThreeControlsProps : PropsWithChildren {
    public var canvasOptions: Canvas3DOptions
    public var vision: Vision?
    public var selected: Name?
    public var onSelect: (Name) -> Unit
}

@JsExport
public val ThreeControls: FC<ThreeControlsProps> = fc { props ->
    tabPane(if (props.selected != null) "Properties" else null) {
        tab("Canvas") {
            card("Canvas configuration") {
                canvasControls(props.canvasOptions, props.vision)
            }
        }
        tab("Tree") {
            css {
                border = Border(1.px, BorderStyle.solid, Color.lightGray)
                padding = Padding(10.px)
            }
            h2 { +"Object tree" }
            styledDiv {
                css {
                    flex = Flex(1.0, 1.0, FlexBasis.inherit)
                }
                props.vision?.let {
                    visionTree(it, props.selected, props.onSelect)
                }
            }
        }
        tab("Properties") {
            props.selected.let { selected ->
                val selectedObject: Vision? = when {
                    selected == null -> null
                    selected.isEmpty() -> props.vision
                    else -> (props.vision as? SolidGroup)?.get(selected)
                }
                if (selectedObject != null) {
                    visionPropertyEditor(selectedObject, key = selected)
                }
            }
        }
        this.parentBuilder.run {
            props.children()
        }
    }
}

public fun RBuilder.threeControls(
    canvasOptions: Canvas3DOptions,
    vision: Vision?,
    selected: Name?,
    onSelect: (Name) -> Unit = {},
    builder: TabBuilder.() -> Unit = {},
): Unit = child(ThreeControls) {
    attrs {
        this.canvasOptions = canvasOptions
        this.vision = vision
        this.selected = selected
        this.onSelect = onSelect
    }
    TabBuilder(this).builder()
}