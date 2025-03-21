package space.kscience.visionforge.three

import space.kscience.dataforge.misc.DFExperimental
import space.kscience.visionforge.html.*
import space.kscience.visionforge.solid.Solids
import java.awt.Desktop
import java.nio.file.Path


public val VisionPage.Companion.threeJsHeader: HtmlFragment get() = scriptHeader("js/visionforge-three.js")

@DFExperimental
public fun Solids.makeThreeJsFile(
    path: Path? = null,
    title: String = "VisionForge page",
    resourceLocation: ResourceLocation = ResourceLocation.SYSTEM,
    show: Boolean = true,
    content: HtmlVisionFragment,
): Unit {
    val actualPath = VisionPage(visionManager, content = content).makeFile(path) { actualPath ->
        mapOf(
            "title" to VisionPage.title(title),
            "threeJs" to VisionPage.importScriptHeader(
                "js/visionforge-three.js",
                resourceLocation,
                actualPath,
            )
        )
    }
    if (show) Desktop.getDesktop().browse(actualPath.toFile().toURI())
}