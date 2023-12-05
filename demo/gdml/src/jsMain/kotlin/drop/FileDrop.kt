@file:JsModule("react-file-drop")
@file:JsNonModule

package drop

import org.w3c.dom.DragEvent
import org.w3c.files.FileList
import react.Component
import react.Props
import react.State

sealed external class DropEffects {
    @JsName("copy")
    object Copy : DropEffects

    @JsName("move")
    object Move : DropEffects

    @JsName("link")
    object Link : DropEffects

    @JsName("none")
    object None : DropEffects
}

external interface FileDropProps : Props {
    var className: String?
    var targetClassName: String?
    var draggingOverFrameClassName: String?
    var draggingOverTargetClassName: String?

    //    var frame?: Exclude<HTMLElementTagNameMap[keyof HTMLElementTagNameMap], HTMLElement> | HTMLDocument;
    var onFrameDragEnter: ((event: DragEvent) -> Unit)?
    var onFrameDragLeave: ((event: DragEvent) -> Unit)?
    var onFrameDrop: ((event: DragEvent) -> Unit)?

    //    var onDragOver: ReactDragEventHandler<HTMLDivElement>?
//    var onDragLeave: ReactDragEventHandler<HTMLDivElement>?
    var onDrop: ((files: FileList?, event: dynamic) -> Unit)?//event:DragEvent<HTMLDivElement>)
    var dropEffect: DropEffects?
}

external interface FileDropState : State {
    var draggingOverFrame: Boolean
    var draggingOverTarget: Boolean
}

external class FileDrop : Component<FileDropProps, FileDropState> {
    override fun render(): dynamic
}