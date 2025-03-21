package space.kscience.plotly.script

import kotlinx.html.TagConsumer
import mu.KLogger
import mu.KotlinLogging
import space.kscience.plotly.Plotly
import space.kscience.plotly.UnstablePlotlyAPI
import space.kscience.plotly.cdnPlotlyHeader
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.VisionPage
import space.kscience.visionforge.html.appendTo
import java.io.File
import kotlin.reflect.typeOf
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

@UnstablePlotlyAPI
public fun Plotly.fragment(
    source: SourceCode,
    logger: KLogger = KotlinLogging.logger("scripting")
): HtmlFragment {

    val workspaceScriptConfiguration = ScriptCompilationConfiguration {
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
    }

    return HtmlFragment {
        val evaluationConfiguration = ScriptEvaluationConfiguration {
            implicitReceivers(this@fragment)
        }
        BasicJvmScriptingHost().eval(source, workspaceScriptConfiguration, evaluationConfiguration).onFailure {
            it.reports.forEach { scriptDiagnostic ->
                when (scriptDiagnostic.severity) {
                    ScriptDiagnostic.Severity.FATAL, ScriptDiagnostic.Severity.ERROR -> {
                        logger.error(scriptDiagnostic.exception) { scriptDiagnostic.toString() }
                        error(scriptDiagnostic.toString())
                    }

                    ScriptDiagnostic.Severity.WARNING -> logger.warn { scriptDiagnostic.toString() }
                    ScriptDiagnostic.Severity.INFO -> logger.info { scriptDiagnostic.toString() }
                    ScriptDiagnostic.Severity.DEBUG -> logger.debug { scriptDiagnostic.toString() }
                }
            }
        }
    }
}

@UnstablePlotlyAPI
public fun Plotly.page(
    file: File,
    title: String = "Plotly.kt",
    headers: Map<String, HtmlFragment> = emptyMap(),
    logger: KLogger = KotlinLogging.logger("scripting")
): VisionPage = VisionPage(
    visionManager = plugin.visionManager,
    pageHeaders = mapOf("title" to VisionPage.title(title), "plotly" to cdnPlotlyHeader) + headers,
) {
    fragment(file.toScriptSource(), logger).appendTo(this)
}


@OptIn(UnstablePlotlyAPI::class)
public fun Plotly.page(
    string: String,
    title: String = "Plotly.kt",
    headers: Map<String, HtmlFragment> = mapOf("plotly" to cdnPlotlyHeader),
    logger: KLogger = KotlinLogging.logger("scripting")
): VisionPage = VisionPage(
    visionManager = plugin.visionManager,
    pageHeaders = mapOf("title" to VisionPage.title(title), "plotly" to cdnPlotlyHeader) + headers,
) {
    fragment(string.toScriptSource(), logger).appendTo(this)
}