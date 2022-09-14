plugins {
    application
}

application {
    mainClass.set("ch.unibas.dmi.dbis.collector.cli.MainKt")
}

dependencies {
    implementation(project(":collector-core"))

    implementation(libs.kotlin.stdlib)
}
