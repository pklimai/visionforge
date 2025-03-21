plugins {
    id("space.kscience.gradle.mpp")
}

val dataforgeVersion: String by rootProject.extra

kscience {
    jvm()
    js()
    native {}
//    wasm()
    useCoroutines()
    commonMain {
        api("space.kscience:dataforge-context:$dataforgeVersion")
        api(project.dependencies.platform(spclibs.ktor.bom))
        api(spclibs.kotlinx.html)
    }
    jsMain {
        api("org.jetbrains.kotlin-wrappers:kotlin-js")
    }
    useSerialization {
        json()
    }
}

readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}