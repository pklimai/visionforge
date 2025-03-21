package space.kscience.plotly.script

import kotlinx.html.TagConsumer
import kotlin.reflect.typeOf
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    fileExtension = "plotly.kts",
    compilationConfiguration = PlotlyScriptCompilationConfiguration::class
)
public abstract class PlotlyScript


public class PlotlyScriptCompilationConfiguration: ScriptCompilationConfiguration({
    baseClass(PlotlyScript::class)
    implicitReceivers(typeOf<TagConsumer<*>>())
    defaultImports(
        "kotlin.math.*",
        "space.kscience.plotly.*",
        "space.kscience.plotly.models.*",
        "space.kscience.dataforge.meta.*",
        "kotlinx.html.*"
    )
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
        compilerOptions.append("-jvm-target", Runtime.version().feature().toString())
    }
    hostConfiguration(defaultJvmScriptingHostConfiguration)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
})