package space.kscience.visionforge.gdml.demo

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Style
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Document
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.context.request
import space.kscience.gdml.GdmlShowCase
import space.kscience.visionforge.Application
import space.kscience.visionforge.Colors
import space.kscience.visionforge.compose.TreeStyles
import space.kscience.visionforge.gdml.toVision
import space.kscience.visionforge.solid.Solids
import space.kscience.visionforge.solid.ambientLight
import space.kscience.visionforge.solid.invoke
import space.kscience.visionforge.solid.three.ThreePlugin
import space.kscience.visionforge.startApplication


private class GDMLDemoApp : Application {

    override fun start(document: Document, state: Map<String, Any>) {
        val context = Context("gdml-demo") {
            plugin(ThreePlugin)
        }

        val element = document.getElementById("application") ?: error("Element with id 'application' not found on page")

        val vision = GdmlShowCase.cubes().toVision().apply {
            ambientLight {
                color(Colors.white)
            }
        }

        renderComposable(element) {
            Style(TreeStyles)
            Style {
                "html" {
                    height(100.percent)
                }

                "body" {
                    height(100.percent)
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Stretch)
                }

                "#application" {
                    width(100.percent)
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Stretch)
                }
            }
            GDMLApp(context.request(Solids), vision)
        }
    }
}

fun main() {
    startApplication(::GDMLDemoApp)
}