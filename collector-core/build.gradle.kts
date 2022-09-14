import de.undercouch.gradle.tasks.download.Download
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

// This is necessary because IntelliJ doesn't properly recognize the alias from the version catalog.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.openapi.generator)
}

sourceSets.getByName("main") {
    java.srcDir("src/main/kotlin")
    java.srcDir("cineast/src/main/kotlin")
}

tasks.register<Download>("downloadCineastOas") {
    doLast {
        src("http://localhost:4567/openapi-specs")
        dest("$buildDir/downloads/cineast-openapi.json")
    }
}

tasks.register<GenerateTask>("generateCineastClient") {
    doLast {
        generatorName.set("kotlin")
        inputSpec.set("$buildDir/downloads/cineast-openapi.json")
        packageName.set("ch.unibas.dmi.dbis.cineast.client")
        outputDir.set("$projectDir/cineast")

        skipValidateSpec.set(true)

        configOptions.set(
            mapOf(
                "enumPropertyNaming" to "UPPERCASE",
                "library" to "jvm-okhttp4",
                "serializationLibrary" to "jackson"
            )
        )
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlin.logging)
    implementation(libs.log4j.slf4j.impl)

    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.retrofit)
    implementation(libs.retrofit.jackson)

    implementation(libs.okhttp3)
    implementation(libs.okhttp3.interceptor)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)

    implementation(libs.hikari)

    implementation(libs.postgres.jdbc)

    implementation(libs.threeten.extra)
}
