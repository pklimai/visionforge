plugins {
    id("space.kscience.gradle.mpp")
}

val dataforgeVersion: String by rootProject.extra

kscience {
    jvm()
    js()
    native()
    wasm()
    useCoroutines()
    commonMain {
        api("space.kscience:dataforge-context:$dataforgeVersion")
        api("com.benasher44:uuid:0.8.4")
        api(spclibs.kotlinx.html)
    }
    jsMain {
        api("org.jetbrains.kotlin-wrappers:kotlin-extensions")
    }
    useSerialization {
        json()
    }
}

readme {
    maturity = space.kscience.gradle.Maturity.DEVELOPMENT
}