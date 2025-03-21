plugins {
    id("space.kscience.gradle.mpp")
    application
    `maven-publish`
}

val ktorVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

kscience {
    jvm()
    jvmMain {
        api(projects.plotlyKt.plotlyKtCore)
        api(spclibs.kotlinx.html)
        api(kotlin("scripting-jvm-host"))
        api(kotlin("scripting-jvm"))
        api("io.github.microutils:kotlin-logging:3.0.5")
        implementation(spclibs.logback.classic)
        implementation(spclibs.kotlinx.cli)
    }
}


application {
    mainClass.set("space.kscience.plotly.script.CliKt")
}