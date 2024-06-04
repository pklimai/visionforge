plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience{
    jvm()
    js()
    native()
    wasm()
    dependencies {
        api(projects.plotly.plotlyktCore)
    }
}

readme{
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}