plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("org.jetbrains.compose") version "1.5.10" apply false
}

group = "ir.amirab"
version = "1.0.4"

repositories {
    mavenCentral()
}
subprojects{
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        val optIns= listOf<String>(
        ).map {
            "-Xopt-in=$it"
        }
        val contextReceivers="-Xcontext-receivers"
        kotlinOptions {
            freeCompilerArgs += optIns+contextReceivers
        }
    }
}