package ringui.header

import react.RBuilder
import react.RClass
import react.RHandler
import react.dom.WithClassName
import ringui.RingUI

@JsModule("@jetbrains/ring-ui/components/header/header")
internal external object HeaderModule {
    val RerenderableHeader: RClass<HeaderProps>
    val Logo: RClass<HeaderLogoProps>
    val Tray: RClass<HeaderTrayProps>
    val TrayIcon: RClass<WithClassName>
    val Profile: RClass<WithClassName>
    val SmartProfile: RClass<WithClassName>
    val Services: RClass<WithClassName>
    val SmartServices: RClass<WithClassName>
}

// https://github.com/JetBrains/ring-ui/blob/master/components/header/header.js
public external interface HeaderProps : WithClassName {
    public var spaced: Boolean
    public var theme: String
}

public fun RBuilder.ringHeader(handler: RHandler<HeaderProps>) {
    RingUI.Header {
        handler()
    }
}
