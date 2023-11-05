plugins{
    kotlin("jvm")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
}

dependencies{
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":lib"))
}
compose{
    desktop{
        application{
            mainClass = "ir.amirab.MainKt"
            nativeDistributions {
                modules("jdk.unsupported")
            }
        }
    }
}