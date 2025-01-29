plugins{
    id("org.jetbrains.changelog")
}


allprojects {
    group = "space.kscience"
    version = "0.8.0"
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}