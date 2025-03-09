plugins{
    id("org.jetbrains.changelog")
}


allprojects {
    group = "space.kscience"
    version = "0.7.2"
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}