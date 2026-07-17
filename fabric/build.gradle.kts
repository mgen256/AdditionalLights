plugins {
    id("net.fabricmc.fabric-loom")
}

import org.gradle.api.file.DuplicatesStrategy
import com.mgen256.conventions.VerifyFabricLootTablesTask
import org.gradle.language.base.plugins.LifecycleBasePlugin

val fabricModId: String = project.property("mod_id").toString()

loom {
    mods {
        create(fabricModId) {
            sourceSet(sourceSets.main.get())
            sourceSet(project(":common").sourceSets.main.get())
        }
    }

    runConfigs.configureEach {
        runDir = "run-dev"
    }
}

val fabricDatagenOutput = layout.projectDirectory.dir("src/main/generated")
fabricApi {
    configureDataGeneration {
        outputDirectory = fabricDatagenOutput.asFile
        createRunConfiguration = true
    }
}

dependencies {
    implementation(project(":common"))
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    compileOnly(libs.modmenu)
    runtimeOnly(libs.modmenu)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

sourceSets["main"].resources.srcDir(rootProject.layout.buildDirectory.dir("generated/datagen/resources"))
val rootGeneratedFabric = rootProject.layout.buildDirectory.dir("generated/fabric")
sourceSets["main"].resources.srcDir(rootGeneratedFabric)

tasks.named<ProcessResources>("processResources") {
    dependsOn(rootProject.tasks.named("generateFabricMetadata"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(rootGeneratedFabric)
    from("platform_resources/assets/additional_lights/fabric") {
        into("assets/additional_lights")
    }
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(rootProject.tasks.named("generateFabricMetadata"))
}

tasks.named<Jar>("jar") {
    dependsOn(":common:classes")
    from(project(":common").sourceSets.main.get().output)
}

val verifyFabricLootTables = tasks.register<VerifyFabricLootTablesTask>("verifyFabricLootTables") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Verify Fabric block loot tables in the distribution jar"
    jarFile.set(tasks.named<Jar>("jar").flatMap { it.archiveFile })
    generatedSpecSources.set(rootProject.layout.buildDirectory.dir("generated/datagen/java/com/mgen256/al"))
    modId.set(fabricModId)
    dependsOn(rootProject.tasks.named("generateBlockData"))
}

tasks.named("check") {
    dependsOn(verifyFabricLootTables)
}

tasks.named<Jar>("jar") {
    finalizedBy(verifyFabricLootTables)
}

tasks.named<JavaExec>("runClient") {
    dependsOn(":common:classes")
    systemProperty("oshi.util.wmi.timeout", 2000)
}

tasks.register("runFabricClient") {
    group = "fabric"
    description = "Run the Fabric client"
    dependsOn(":fabric:runClient")
}
