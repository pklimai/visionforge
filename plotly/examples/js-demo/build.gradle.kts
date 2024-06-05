plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets{
        jsMain{
            dependencies{
                implementation(projects.plotly.plotlyktCore)
                implementation(spclibs.kotlinx.coroutines.core)
            }
        }
    }
}