package space.kscience.visionforge.html

import kotlinx.browser.document
import org.w3c.dom.Document

public fun startApplication(start: (Document) -> Unit) {
    if (document.body != null) {
        start(document)
    } else {
        document.addEventListener("DOMContentLoaded", { start(document) })
    }
}