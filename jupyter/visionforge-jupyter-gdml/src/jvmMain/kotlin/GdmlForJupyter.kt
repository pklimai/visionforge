package space.kscience.visionforge.gdml.jupyter

import org.jetbrains.kotlinx.jupyter.api.libraries.resources
import space.kscience.dataforge.context.Context
import space.kscience.dataforge.misc.DFExperimental
import space.kscience.gdml.Gdml
import space.kscience.visionforge.gdml.toVision
import space.kscience.visionforge.jupyter.VFIntegrationBase
import space.kscience.visionforge.solid.Solids
import space.kscience.visionforge.visionManager

@DFExperimental
internal class GdmlForJupyter : VFIntegrationBase(
    Context("GDML") {
        plugin(Solids)
    }.visionManager
) {

    override fun Builder.afterLoaded() {

        resources {
            js("three") {
                classPath("js/gdml-jupyter.js")
            }
        }

        import(
            "space.kscience.gdml.*",
            "space.kscience.visionforge.gdml.jupyter.*"
        )

        render<Gdml> { gdmlModel ->
            handler.produceHtml {
                vision { gdmlModel.toVision() }
            }
        }
    }
}
