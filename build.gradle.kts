// This is necessary because IntelliJ doesn't properly recognize the alias from the version catalog.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kt.jvm)
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "java")
    apply(plugin = "idea")

    group = "ch.unibas.dmi.dbis"
    version = "0.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
