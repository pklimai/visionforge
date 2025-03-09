@file:JsModule("bootstrap")
@file:JsNonModule

package bootstrap

import org.w3c.dom.HTMLDivElement

internal external class Modal(element: HTMLDivElement) {
    internal fun show()
    internal fun hide()
    internal fun dispose()
}

internal external class Toast(element: HTMLDivElement) {
    internal fun show()
    internal fun dispose()
}

internal external class Offcanvas(element: HTMLDivElement) {
    internal fun show()
    internal fun hide()
}

internal external val needsJS: dynamic
