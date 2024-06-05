package space.kscience.plotly

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import space.kscience.visionforge.html.HtmlFragment
import space.kscience.visionforge.html.appendTo


public val cdnPlotlyHeader: HtmlFragment = HtmlFragment {
    script {
        type = "text/javascript"
        src = Plotly.PLOTLY_CDN
    }
}

public fun FlowContent.plotly(
    plot: Plot,
    config: PlotlyConfig = PlotlyConfig(),
    plotId: String = "plotly[${plot.hashCode().toUInt().toString(16)}]",
) {
    div {
        id = plotId
        script {
            val tracesString = plot.data.toJsonString()
            val layoutString = plot.layout.toJsonString()
            unsafe {
                //language=JavaScript
                +"""
                        Plotly.react(
                            $plotId,
                            $tracesString,
                            $layoutString,
                            $config
                        );
                    """.trimIndent()
            }
        }
    }
}

/**
 * Create an html (including headers) string from plot
 */
public fun Plot.toHTMLPage(
    vararg headers: HtmlFragment = arrayOf(cdnPlotlyHeader),
    config: PlotlyConfig = PlotlyConfig(),
): String = createHTML().html {
    head {
        meta {
            charset = "utf-8"
        }
        title(layout.title ?: "Plotly.kt")
        headers.forEach {
            it.appendTo(consumer)
        }
    }
    body {
        plotly(this@toHTMLPage, config)
    }
}

public val mathJaxHeader: HtmlFragment = HtmlFragment {
    script {
        type = "text/x-mathjax-config"
        unsafe {
            +"""
            MathJax.Hub.Config({
                tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
            });
            """
        }
    }
    script {
        type = "text/javascript"
        async = true
        src = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_SVG"
    }
}