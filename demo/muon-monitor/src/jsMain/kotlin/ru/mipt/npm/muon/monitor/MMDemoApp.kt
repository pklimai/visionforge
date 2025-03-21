package ru.mipt.npm.muon.monitor

import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.request
import space.kscience.visionforge.VisionManager
import space.kscience.visionforge.html.VisionForgeStyles
import space.kscience.visionforge.html.startApplication
import space.kscience.visionforge.solid.three.ThreePlugin


fun main(): Unit = startApplication {
    val context = Context("MM-demo") {
        plugin(ThreePlugin)
    }

    val visionManager = context.request(VisionManager)

    val model = Model(visionManager)

    renderComposable("app") {
        Style(VisionForgeStyles)
        MMApp(context, model)
    }
}
