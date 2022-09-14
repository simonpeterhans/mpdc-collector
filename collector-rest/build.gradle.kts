import de.undercouch.gradle.tasks.download.Download

plugins {
    application
}

application {
    mainClass.set("ch.unibas.dmi.dbis.collector.rest.APIKt")
}

// Requires the API running on port 5678.
tasks.register<Download>("downloadCineastOas") {
    doLast {
        src("http://localhost:5678/openapi-specs")
        dest("$rootDir/oas/mpdc-collector-oas.json")
    }
}

dependencies {
    implementation(project(":collector-core"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlin.logging)
    implementation(libs.log4j.slf4j.impl)

    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.javalin)
    implementation(libs.javalin.openapi)

    implementation(libs.threeten.extra)
}
