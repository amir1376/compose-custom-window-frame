plugins{
    kotlin("jvm")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
}

dependencies{
    implementation(compose.desktop.common)
}