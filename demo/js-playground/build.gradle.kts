plugins {
    id("space.kscience.gradle.mpp")
    alias(spclibs.plugins.compose.compiler)
    alias(spclibs.plugins.compose.jb)
}

kscience {
    useCoroutines()
}

kotlin {
    explicitApi = null
    js {
        browser {
            binaries.executable()
            commonWebpackConfig{
                cssSupport{
                    enabled = true
                }
                scssSupport{
                    enabled = true
                }
                sourceMaps = true
            }
        }
    }
}

kscience {
    dependencies {
        implementation(projects.plotlyKt.plotlyKtCore)
        implementation(projects.visionforge.visionforgeGdml)
        implementation(projects.visionforge.visionforgeMarkdown)
        implementation(projects.visionforge.visionforgeThreejs)
    }
}