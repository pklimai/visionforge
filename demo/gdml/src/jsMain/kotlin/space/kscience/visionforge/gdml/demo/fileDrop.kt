package space.kscience.visionforge.gdml.demo

import drop.FileDrop
import kotlinx.css.*
import org.w3c.files.FileList
import react.RBuilder
import styled.css
import styled.styledDiv

//TODO move styles to inline

fun RBuilder.fileDrop(title: String, action: (files: FileList?) -> Unit) {
    styledDiv {
        css {
            border = Border(style = BorderStyle.dashed, width = 1.px, color = Color.orange)
            flexGrow = 0.0
            alignContent = Align.center
        }

        child(FileDrop::class) {
            attrs {
                onDrop = { files, _ ->
                    console.info("loaded $files")
                    action(files)
                }
            }
            +title
        }
    }
}