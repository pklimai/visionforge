package space.kscience.visionforge.html

import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.meta
import kotlinx.html.stream.createHTML
import space.kscience.dataforge.context.Global
import space.kscience.visionforge.visionManager
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

/**
 * Render given [VisionPage] to a string using a set of [additionalHeaders] that override current page headers.
 */
public fun VisionPage.makeString(additionalHeaders: Map<String, HtmlFragment> = emptyMap()): String = createHTML().apply {
    head {
        meta {
            charset = "utf-8"
        }
        (pageHeaders + additionalHeaders).values.forEach {
            appendFragment(it)
        }
    }
    body {
        visionFragment(Global.visionManager, fragment = content)
    }
}.finalize()

/**
 * Export a [VisionPage] to a file
 *
 * @param fileHeaders additional file-system-specific headers.
 */
public fun VisionPage.makeFile(
    path: Path?,
    fileHeaders: ((Path) -> Map<String, HtmlFragment>) = { emptyMap() },
): Path {
    val actualFile = path ?: Files.createTempFile("vfPage", ".html")

    val htmlString = makeString(fileHeaders(actualFile))

    Files.writeString(actualFile, htmlString)

    return actualFile
}

public fun VisionPage.openInBrowser(path: Path? = null) {
    val actualPath = makeFile(path)
    Desktop.getDesktop().browse(actualPath.toFile().toURI())
}