plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience{
    jvm()
    js()
    dependencies {
        api(projects.visionforgeSolid)
    }
    useSerialization {
        json()
    }
}