pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.neoforged.net/releases")
        maven(url = "https://maven.parchmentmc.org/")
    }

    plugins {
        id("net.fabricmc.fabric-loom") version "1.17.11"
        id("net.neoforged.moddev") version "2.0.141"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val isWindowsHost = System.getProperty("os.name", "")
    .startsWith("Windows", ignoreCase = true)
val hasCustomTrustStore = System.getProperty("javax.net.ssl.trustStore") != null
    || System.getProperty("javax.net.ssl.trustStoreType") != null
if (isWindowsHost && !hasCustomTrustStore) {
    System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT")
}

rootProject.name = "additional_lights"

include("common", "fabric", "neoforge", "tools")
