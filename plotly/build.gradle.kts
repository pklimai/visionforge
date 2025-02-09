plugins{
    id("org.jetbrains.changelog")
}

val plotlyVersion: String by rootProject.extra

allprojects {
    group = "space.kscience"
    version = plotlyVersion
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}