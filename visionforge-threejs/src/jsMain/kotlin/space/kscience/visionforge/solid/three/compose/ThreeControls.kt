package space.kscience.visionforge.solid.three.compose

import androidx.compose.runtime.Composable
import bootstrap.Button
import bootstrap.Color
import bootstrap.Column
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.Hr
import space.kscience.dataforge.context.Global
import space.kscience.dataforge.names.Name
import space.kscience.visionforge.Vision
import space.kscience.visionforge.encodeToString
import space.kscience.visionforge.html.*
import space.kscience.visionforge.solid.specifications.Canvas3DOptions

//private val fileSaver = importAsync<dynamic>("file-saver")

@Composable
internal fun CanvasControls(
    vision: Vision?,
    options: Canvas3DOptions,
) {
    Column {
        vision?.let { vision ->
            Button("Export", color = Color.Info, styling = { Layout.width = bootstrap.Layout.Width.Full }) {
                val json = vision.encodeToString()

                Global.launch {
                    FileKit.saveFile(
                        baseName = options.canvasName,
                        extension = "json",
                        bytes = json.encodeToByteArray()
                    )
                }

//                val blob = Blob(arrayOf(json), BlobPropertyBag("text/json;charset=utf-8"))
//
//                fileSaver.then {
//                    it.saveAs(blob, "${options.canvasName}.json") as Unit
//                }
            }
        }
        Hr()
        PropertyEditor(
            properties = options.meta,
            descriptor = Canvas3DOptions.descriptor,
            expanded = false
        )

    }
}


@Composable
public fun ThreeControls(
    vision: Vision?,
    canvasOptions: Canvas3DOptions,
    selected: Name?,
    onSelect: (Name?) -> Unit,
    tabBuilder: @Composable TabsBuilder.() -> Unit = {},
) {
    Tabs(
        styling = {
            Layout.height = bootstrap.Layout.Height.Full
        }
    ) {
        vision?.let { vision ->
            Tab("Tree") {
                CardTitle("Vision tree")
                VisionTree(vision, Name.EMPTY, selected, onSelect)
            }
        }
        Tab("Settings") {
            CardTitle("Canvas configuration")
            CanvasControls(vision, canvasOptions)
        }
        tabBuilder()
    }
}
