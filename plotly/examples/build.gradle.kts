plugins {
    kotlin("jvm")
}

repositories {
    maven("https://repo.kotlin.link")
    mavenCentral()
}

dependencies {
    implementation(projects.plotly.plotlyktServer)
    implementation(projects.plotly.plotlyktJupyter)
    implementation(projects.plotly.plotlyktScript)
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlinx:dataframe:0.13.1")
}

kotlin{
    jvmToolchain(11)
}

// A workaround for https://youtrack.jetbrains.com/issue/KT-44101

val copyPlotlyResources by tasks.creating(Copy::class){
    dependsOn(":plotly:plotlykt-core:jvmProcessResources")
    mustRunAfter(":plotly:plotlykt-core:jvmTestProcessResources")
    from(project(":plotly:plotlykt-core").layout.buildDirectory.file("processedResources/jvm"))
    into(layout.buildDirectory.file("resources"))
}

tasks.getByName("classes").dependsOn(copyPlotlyResources)

//val runDynamicServer by tasks.creating(JavaExec::class){
//    group = "run"
//    classpath = sourceSets["main"].runtimeClasspath
//    main = "DynamicServerKt"
//}
//
//val runCustomPage by tasks.creating(JavaExec::class){
//    group = "run"
//    classpath = sourceSets["main"].runtimeClasspath
//    main = "CustomPageKt"
//}