
plugins {
    base
}

import org.gradle.api.plugins.BasePluginExtension
import org.gradle.language.base.plugins.LifecycleBasePlugin
import com.mgen256.conventions.VerifyBlockDataTask





val mod_version: String = project.property("mod_version") as String
val mod_id: String = project.property("mod_id") as String
val minecraft_version = libs.versions.minecraft.get()
val neoVersion = libs.versions.neoforge.get()
val active_platform: String = (
    project.findProperty("active_platform")
        ?: project.findProperty("loom.platform")
        ?: "fabric"
).toString()
val releaseBuildTaskNames = setOf(
    "buildReleaseFabric",
    "buildReleaseNeoforge",
    "buildReleaseAll"
)
val releaseBuildRequested = gradle.startParameter.taskNames
    .map { it.substringAfterLast(':') }
    .any { it in releaseBuildTaskNames }

val generatedNeoRes = layout.buildDirectory.dir("generated/neoforge")
val generatedFabricRes = layout.buildDirectory.dir("generated/fabric")





val rootGroup = "com.mgen256"
group = rootGroup

subprojects {
    group = rootGroup

    version = mod_version

    if (name == "fabric" || name == "neoforge") {
        pluginManager.withPlugin("base") {
            extensions.configure<BasePluginExtension> {
                archivesName = "${mod_id}-${project.name}-${minecraft_version}"
            }
        }
    }

    if (name != "tools") {
        apply(plugin = "com.mgen256.conventions.java-mod-common")
    }

    tasks.withType<ProcessResources>().configureEach {
        dependsOn(rootProject.tasks.named("generateBlockData"))
        dependsOn(rootProject.tasks.named("generateModMetadata"))
        dependsOn(rootProject.tasks.named("generateFabricMetadata"))
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.parchmentmc.org/")
        maven("https://maven.terraformersmc.com/releases/")
        maven("https://maven.shedaniel.me/")
        mavenLocal()
    }

    tasks.withType<JavaCompile>().configureEach {
        dependsOn(rootProject.tasks.named("generateBlockData"))
        if (releaseBuildRequested) {
            options.isDebug = false
        }
    }

}


tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}



tasks.register<com.mgen256.conventions.PythonToolTask>("loomScan") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Run Loom --scan equivalent JSON structure validation"
    module.set("tools.loom_scan")
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("namingLint") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Validate block_definitions.csv and Python class names"
    module.set("tools.naming_lint")
    args.set(listOf("--check-class-names"))
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("checkLang") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Validate key consistency in lang JSON files"
    module.set("tools.check_lang_consistency")
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("assetLint") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Validate missing or extra assets from block_definitions.csv"
    module.set("tools.asset_lint")
    dependsOn("generateBlockData")
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("prepareBlockbenchPack") {
    group = "blockbench"
    description = "Prepare the Blockbench working resource pack (run/blockbench_pack)"
    module.set("tools.blockbench_pack")
    args.set(listOf("prepare", "--overwrite-generated"))
    dependsOn("generateBlockData")
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("refreshBlockbenchPack") {
    group = "blockbench"
    description = "Refresh the Blockbench working resource pack (run/blockbench_pack) from common"
    module.set("tools.blockbench_pack")
    args.set(listOf("prepare", "--overwrite-mod", "--overwrite-generated"))
    dependsOn("generateBlockData")
    dependsOn(":tools:installPythonDeps")
}

tasks.register<com.mgen256.conventions.PythonToolTask>("pushBlockbenchPack") {
    group = "blockbench"
    description = "Apply Blockbench working pack edits back to common"
    module.set("tools.blockbench_pack")
    args.set(listOf("push", "--overwrite"))
    dependsOn(":tools:installPythonDeps")
}

tasks.register("lint") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Run Python lint, asset checks, and pytest"
    dependsOn(tasks.named("namingLint"))
    dependsOn(tasks.named("loomScan"))
    dependsOn(tasks.named("checkLang"))
    dependsOn(tasks.named("assetLint"))
    dependsOn(":tools:test")
}

tasks.register<BlockDataGeneratorTask>("generateBlockData") {
    group = "generation"
    description = "Generate ModBlockList.java and JSON from CSV definitions"
    definitions.set(rootProject.layout.projectDirectory.file("tools/block_definitions.csv"))
    scriptFile.set(rootProject.layout.projectDirectory.file("tools/datagen.py"))
    val genDir = rootProject.layout.buildDirectory.dir("generated/datagen")
    outputDir.set(genDir)
    outputDirs.put("java", genDir.map { it.dir("java") })
    outputDirs.put("resources", genDir.map { it.dir("resources") })
    dependsOn(":tools:installPythonDeps")
}

tasks.register<VerifyBlockDataTask>("verifyBlockData") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Verify generated outputs against CSV definitions"
    val generateBlockDataTask = tasks.named<BlockDataGeneratorTask>("generateBlockData")
    script.set(rootProject.layout.projectDirectory.file("tools/datagen.py"))
    definitions.set(rootProject.layout.projectDirectory.file("tools/block_definitions.csv"))
    generatedJava.set(generateBlockDataTask.flatMap { it.outputDirs.getting("java") })
    generatedResources.set(generateBlockDataTask.flatMap { it.outputDirs.getting("resources") })
    args.addAll(listOf("--gen-dir", generateBlockDataTask.get().outputDir.get().asFile.absolutePath))
    dependsOn(generateBlockDataTask)
    dependsOn(":tools:installPythonDeps")
}


tasks.register<Copy>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neo_version" to neoVersion,
        "neoforge_version_range" to project.property("neoforge_version_range"),
        "mod_id" to mod_id,
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to mod_version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    inputs.properties(replaceProperties)
    from("neoforge/platform_resources/assets/additional_lights/neoforge") {
        expand(replaceProperties)
    }
    into(generatedNeoRes)
}

tasks.register<Copy>("generateFabricMetadata") {
    val replaceProperties = mapOf(
        "mod_version" to mod_version
    )
    inputs.properties(replaceProperties)
    from("fabric/platform_resources/assets/additional_lights/fabric") {
        expand(replaceProperties)
    }
    into(generatedFabricRes)
}


val fabricBuild = tasks.register("buildFabric") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Run the Fabric build"
    dependsOn(":fabric:build")
}

val neoforgeBuild = tasks.register("buildNeoforge") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Run the NeoForge build"
    dependsOn(":neoforge:build")
}

neoforgeBuild.configure {
    mustRunAfter(fabricBuild)
}

tasks.register("buildAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Build Fabric and NeoForge in order"
    dependsOn(fabricBuild)
    dependsOn(neoforgeBuild)
}

val fabricReleaseBuild = tasks.register("buildReleaseFabric") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Build the Fabric release artifact without Java debug metadata"
    dependsOn(":fabric:build")
}

val neoforgeReleaseBuild = tasks.register("buildReleaseNeoforge") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Build the NeoForge release artifact without Java debug metadata"
    dependsOn(":neoforge:build")
}

neoforgeReleaseBuild.configure {
    mustRunAfter(fabricReleaseBuild)
}

tasks.register("buildReleaseAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Build Fabric and NeoForge release artifacts in order"
    dependsOn(fabricReleaseBuild)
    dependsOn(neoforgeReleaseBuild)
}


val checkAll = tasks.register("checkAll") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Run lint, verifyBlockData, and all Java subproject checks"
    dependsOn(tasks.named("lint"))
    dependsOn(tasks.named("verifyBlockData"))
}

subprojects {
    pluginManager.withPlugin("java") {
        rootProject.tasks.named("checkAll").configure {
            dependsOn(tasks.named("check"))
        }
    }
}
