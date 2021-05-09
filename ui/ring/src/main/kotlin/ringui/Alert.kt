package ringui

import react.RBuilder
import react.RHandler
import react.dom.WithClassName

// https://github.com/JetBrains/ring-ui/blob/master/components/alert/alert.js
public external interface AlertProps : WithClassName {
    public var timeout: Number
    public var onCloseRequest: () -> Unit
    public var onClose: () -> Unit
    public var isShaking: Boolean
    public var isClosing: Boolean
    public var inline: Boolean
    public var showWithAnimation: Boolean
    public var closeable: Boolean
    public var type: AlertType
}

public typealias AlertType = String

public object AlertTypes {
    public var ERROR = "error"
    public var MESSAGE = "message"
    public var SUCCESS = "success"
    public var WARNING = "warning"
    public var LOADING = "loading"
}

public fun RBuilder.ringAlert(handler: RHandler<AlertProps>) {
    RingUI.Alert {
        handler()
    }
}