package space.kscience.visionforge.markup

import org.intellij.lang.annotations.Language

public fun VisionOfMarkup.content(@Language("markdown") text: String) {
    content = text
}