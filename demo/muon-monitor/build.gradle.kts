plugins {
    id("space.kscience.gradle.mpp")
    alias(spclibs.plugins.compose.compiler)
    alias(spclibs.plugins.compose.jb)
//    alias(spclibs.plugins.ktor)
    application
}

group = "ru.mipt.npm"


kscience {
    fullStack(
        "muon-monitor.js",
        jvmConfig = {withJava()},
        browserConfig = {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
                scssSupport {
                    enabled = true
                }
            }
        }
    )

    useCoroutines()
    useSerialization()
    useKtor()

    commonMain {
        implementation(projects.visionforgeSolid)
        implementation(projects.visionforgeComposeHtml)
    }
    jvmMain {
        implementation("org.apache.commons:commons-math3:3.6.1")
        implementation("io.ktor:ktor-server-cio")
        implementation("io.ktor:ktor-server-content-negotiation")
        implementation("io.ktor:ktor-serialization-kotlinx-json")
        implementation(spclibs.logback.classic)
    }
    jsMain {
        implementation(projects.visionforgeThreejs)
        //implementation(devNpm("webpack-bundle-analyzer", "4.4.0"))
    }
}
kotlin {
    explicitApi = null
}


application {
    mainClass.set("ru.mipt.npm.muon.monitor.MMServerKt")
}