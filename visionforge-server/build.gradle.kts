plugins {
    id("space.kscience.gradle.mpp")
}

kscience{
    jvm()
    jvmMain {
        api(projects.visionforgeCore)
        api(project.dependencies.platform(spclibs.ktor.bom))
        api("io.ktor:ktor-server-host-common")
        api("io.ktor:ktor-server-html-builder")
        api("io.ktor:ktor-server-websockets")
        api("io.ktor:ktor-server-cors")
    }
}

