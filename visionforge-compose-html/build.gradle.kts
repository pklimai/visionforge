plugins {
    id("space.kscience.gradle.mpp")
    alias(spclibs.plugins.compose)
}

kscience {
    js()
    jvm()
}

kotlin {
//    android()
    sourceSets {
        commonMain {
            dependencies {
                api(projects.visionforgeCore)
                //need this to placate compose compiler in MPP applications
                api(compose.runtime)
            }
        }

        jsMain {
            dependencies {
                implementation(npm("bootstrap", "5.3.3"))
                implementation(npm(" bootstrap-icons", "1.11.3"))
                api("com.benasher44:uuid:0.8.4")
                api(compose.html.core)
            }
        }
    }
}