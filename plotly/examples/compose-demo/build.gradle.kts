plugins {
    kotlin("multiplatform")
    alias(spclibs.plugins.compose.compiler)
    alias(spclibs.plugins.compose.jb)
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
    maven("https://jogamp.org/deployment/maven")
}

kotlin {
    jvm()
    jvmToolchain(17)
    sourceSets {
        jvmMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.desktop.currentOs)
                implementation("io.github.kevinnzou:compose-webview-multiplatform:1.9.8")
                implementation(spclibs.logback.classic)
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "space.kscience.plotly.compose.AppKt"
        }
    }
}
