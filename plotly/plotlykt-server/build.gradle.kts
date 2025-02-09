plugins {
    id("space.kscience.gradle.mpp")
    alias(spclibs.plugins.compose.compiler)
    alias(spclibs.plugins.compose.jb)
}

kscience {
    fullStack(
        bundleName = "js/plotly-kt-server.js",
        browserConfig = {
            webpackTask {
                cssSupport {
                    enabled = true
                }
                scssSupport {
                    enabled = true
                }
            }
        }
    )

    commonMain {
        api(projects.visionforgeComposeHtml)
        api(projects.plotly.plotlyktCore)
    }

    jvmMain {
        api(projects.visionforgeServer)
        api("io.ktor:ktor-server-cio")
    }
}