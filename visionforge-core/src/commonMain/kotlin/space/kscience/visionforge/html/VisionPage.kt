package space.kscience.visionforge.html

import kotlinx.html.*
import space.kscience.visionforge.VisionManager

/**
 * A structure representing a single page with Visions to be rendered.
 *
 * @param pageHeaders static headers for this page.
 */
public data class VisionPage(
    public val visionManager: VisionManager,
    public val pageHeaders: Map<String, HtmlFragment> = emptyMap(),
    public val content: HtmlVisionFragment,
) {
    public companion object {
        /**
         * Use a script with given [src] as a global header for all pages.
         */
        public fun scriptHeader(src: String, block: SCRIPT.() -> Unit = {}): HtmlFragment = HtmlFragment {
            script {
                type = "text/javascript"
                this.src = src
                block()
            }
        }

        /**
         * Use css with the given stylesheet link as a global header for all pages.
         */
        public fun styleSheetHeader(href: String, block: LINK.() -> Unit = {}): HtmlFragment = HtmlFragment {
            link {
                rel = "stylesheet"
                this.href = href
                block()
            }
        }

        /**
         * Inject a simple style header
         */
        public fun styleHeader(block: STYLE.() -> Unit): HtmlFragment = HtmlFragment {
            style {
                block()
            }
        }

        public fun title(title: String): HtmlFragment = HtmlFragment {
            title(title)
        }
    }
}

public fun VisionManager.VisionPage(
    vararg headers: HtmlFragment,
    content: HtmlVisionFragment
): VisionPage = VisionPage(this, headers.associateBy { it.toString() }, content)