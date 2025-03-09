plugins {
    id("space.kscience.gradle.mpp")
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


readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}