plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
    implementation(spclibs.logback.classic)
}

javafx{
    modules("javafx.web")
    version = "11"
}

application {
    mainClass.set("space.kscience.plotly.fx.PlotlyFXAppKt")
}

kotlin{
    jvmToolchain(11)
}

