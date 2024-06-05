plugins {
    id("space.kscience.gradle.mpp")
    kotlin("jupyter.api")
    `maven-publish`
}

val dataforgeVersion: String by rootProject.extra
val plotlyVersion by extra("2.29.0")


kotlin{
    applyDefaultHierarchyTemplate()
}

kscience {
    fullStack(bundleName = "js/plotly-kt.js")
    native()
    wasm()
    useSerialization()

    commonMain {
        api(projects.visionforgeCore)
//        api("space.kscience:dataforge-meta:$dataforgeVersion")
//        api(spclibs.kotlinx.html)

    }

    jsMain{
        api(npm("plotly.js", plotlyVersion))
    }

    nativeMain {
        implementation("com.squareup.okio:okio:3.3.0")
    }
}

tasks.processJupyterApiResources{
    libraryProducers = listOf("space.kscience.plotly.PlotlyIntegration")
}

readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}