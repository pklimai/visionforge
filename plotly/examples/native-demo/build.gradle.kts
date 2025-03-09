plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

kotlin {
    linuxX64{
        binaries{
            executable()
        }
    }

    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":plotly:plotlykt-core"))
            }
        }
    }
}