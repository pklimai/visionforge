rootProject.name = "visionforge"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {

    val toolsVersion: String by extra

    repositories {
        //mavenLocal()
        maven("https://repo.kotlin.link")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("ru.mipt.npm.gradle.project") version toolsVersion
        id("ru.mipt.npm.gradle.mpp") version toolsVersion
        id("ru.mipt.npm.gradle.jvm") version toolsVersion
        id("ru.mipt.npm.gradle.js") version toolsVersion
    }
}

dependencyResolutionManagement {

    val toolsVersion: String by extra

    repositories {
        mavenLocal()
        maven("https://repo.kotlin.link")
        mavenCentral()
    }

    versionCatalogs {
        create("npmlibs") {
            from("ru.mipt.npm:version-catalog:$toolsVersion")
        }
    }
}

include(
//    ":ui",
    ":ui:react",
    ":ui:ring",
//    ":ui:material",
    ":ui:bootstrap",
    ":visionforge-core",
    ":visionforge-solid",
    ":visionforge-fx",
    ":visionforge-threejs",
    ":visionforge-threejs:visionforge-threejs-server",
    ":visionforge-gdml",
    ":cern-root-loader",
    ":visionforge-server",
    ":visionforge-plotly",
    ":visionforge-tables",
    ":visionforge-markdown",
    ":demo:solid-showcase",
    ":demo:gdml",
    ":demo:muon-monitor",
    ":demo:sat-demo",
    ":demo:playground",
    ":demo:plotly-fx",
    ":demo:js-playground",
    ":jupyter",
    ":jupyter:visionforge-jupyter-gdml"
)
