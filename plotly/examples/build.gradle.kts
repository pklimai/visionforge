plugins {
    kotlin("jvm")
}

repositories {
    maven("https://repo.kotlin.link")
    mavenCentral()
}

dependencies {
    implementation(projects.plotly.plotlyktServer)
    implementation(projects.plotly.plotlyktScript)
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlinx:dataframe:0.15.0")
}

kotlin{
    jvmToolchain(17)
}

// A workaround for https://youtrack.jetbrains.com/issue/KT-44101

val copyPlotlyResources by tasks.creating(Copy::class){
    dependsOn(":plotly:plotlykt-server:jvmProcessResources")
    mustRunAfter(":plotly:plotlykt-server:jvmTestProcessResources")
    from(project(":plotly:plotlykt-server").layout.buildDirectory.file("processedResources/jvm/main"))
    into(layout.buildDirectory.file("resources/main"))
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