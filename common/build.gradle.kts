plugins {
    `java-library`
    id("net.fabricmc.fabric-loom")
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

sourceSets["main"].java.srcDir(rootProject.layout.buildDirectory.dir("generated/datagen/java"))

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.fabric.loader)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}
