plugins {
    id("space.kscience.gradle.mpp")
}

val tablesVersion = "0.4.1"

kscience {
    jvm()
    js {
        binaries.library()
        browser {
            webpackTask{
                cssSupport {
                    enabled = true
                }
                scssSupport {
                    enabled = true
                }
            }
        }
    }

    useSerialization()
    commonMain {
        api(projects.visionforgeCore)
        api("space.kscience:tables-kt:${tablesVersion}")
    }
    jsMain {
        api(npm("tabulator-tables", "6.3.1"))
        api(npm("@types/tabulator-tables", "6.2.3"))
    }
}

readme {
    maturity = space.kscience.gradle.Maturity.PROTOTYPE
}