plugins {
    id("ru.mipt.npm.gradle.mpp")
    application
}

group = "ru.mipt.npm"

val ktorVersion: String = npmlibs.versions.ktor.get()

kscience {
    useCoroutines()
    useSerialization()
    application()
}

kotlin {
    jvm {
        withJava()
    }
    js {
        useCommonJs()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = false
            }
        }
    }

    afterEvaluate {
        val jsBrowserDistribution by tasks.getting

        tasks.getByName<ProcessResources>("jvmProcessResources") {
            dependsOn(jsBrowserDistribution)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(jsBrowserDistribution)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":visionforge-solid"))
            }
        }
        jvmMain {
            dependencies {
                implementation("org.apache.commons:commons-math3:3.6.1")
                implementation(npmlibs.ktor.server.cio)
                implementation(npmlibs.ktor.serialization)
            }
        }
        jsMain {
            dependencies {
                implementation(project(":ui:ring"))
                implementation(npmlibs.ktor.client.js)
                implementation(npmlibs.ktor.client.serialization)
                implementation(project(":visionforge-threejs"))
                //implementation(devNpm("webpack-bundle-analyzer", "4.4.0"))
            }
        }
    }
}

application {
    mainClass.set("ru.mipt.npm.muon.monitor.server.MMServerKt")
}

//distributions {
//    main {
//        contents {
//            from("$buildDir/libs") {
//                rename("${rootProject.name}-jvm", rootProject.name)
//                into("lib")
//            }
//        }
//    }
//}