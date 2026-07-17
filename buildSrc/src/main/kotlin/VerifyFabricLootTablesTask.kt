package com.mgen256.conventions

import java.io.File
import java.util.zip.ZipFile
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class VerifyFabricLootTablesTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val jarFile: RegularFileProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val generatedSpecSources: DirectoryProperty

    @get:Input
    abstract val modId: Property<String>

    @TaskAction
    fun verify(): Unit {
        val expectedTables: List<LootTableExpectation> = loadExpectations()
        val expectedPaths: Map<String, LootTableExpectation> = expectedTables.associateBy {
            lootTablePath(it.registryName)
        }
        val file: File = jarFile.get().asFile

        ZipFile(file).use { zip: ZipFile ->
            val actualPaths: Set<String> = zip.entries().asSequence()
                .map { entry -> entry.name }
                .filter { path -> path.startsWith(lootTableDirectory()) && path.endsWith(".json") }
                .toSet()
            val missing: Set<String> = expectedPaths.keys - actualPaths
            val unexpected: Set<String> = actualPaths - expectedPaths.keys

            if (missing.isNotEmpty() || unexpected.isNotEmpty()) {
                throw GradleException(
                    "Fabric loot table coverage mismatch in ${file.name}. " +
                        "Missing: ${missing.sorted().joinToString()}. " +
                        "Unexpected: ${unexpected.sorted().joinToString()}."
                )
            }

            expectedPaths.forEach { (path: String, expectation: LootTableExpectation) ->
                val entry = zip.getEntry(path)
                    ?: throw GradleException("Fabric loot table is missing from ${file.name}: $path")
                val content: String = zip.getInputStream(entry)
                    .bufferedReader(Charsets.UTF_8)
                    .use { reader -> reader.readText() }
                verifyTableContent(path, expectation, content)
            }
        }

        logger.lifecycle(
            "Verified Fabric loot tables: jar={}, tables={}, namespace={}",
            file.name,
            expectedPaths.size,
            modId.get()
        )
    }

    private fun loadExpectations(): List<LootTableExpectation> {
        val sourceDirectory: File = generatedSpecSources.get().asFile
        val enumNames: List<String> = loadRegisteredEnumNames(sourceDirectory)
        val specs: List<BlockSpecDefinition> = enumNames.flatMap { enumName: String ->
            loadEnumDefinitions(sourceDirectory, enumName)
        }
        val specsByKey: Map<String, BlockSpecDefinition> = specs.associateBy { it.key }

        if (specsByKey.size != specs.size) {
            throw GradleException("Duplicate block spec key in ${sourceDirectory.absolutePath}.")
        }

        return specs.map { spec: BlockSpecDefinition ->
            val droppedRegistryName: String = if (spec.enumName == "WallTorchSpec") {
                val floorKey: String = spec.linkedTorchKey
                    ?: throw GradleException("Wall torch ${spec.key} does not declare a floor torch key.")
                specsByKey[floorKey]?.registryName
                    ?: throw GradleException("Wall torch ${spec.key} references unknown floor torch $floorKey.")
            } else {
                spec.registryName
            }
            LootTableExpectation(
                registryName = spec.registryName,
                droppedRegistryName = droppedRegistryName,
                requiresUnsummonedCondition = spec.registryName.startsWith("fire_for_")
                    || spec.registryName.startsWith("soul_fire_for_")
            )
        }
    }

    private fun loadRegisteredEnumNames(sourceDirectory: File): List<String> {
        val modBlockList: File = sourceDirectory.resolve("ModBlockList.java")
        if (!modBlockList.isFile) {
            throw GradleException("Generated ModBlockList.java is missing: ${modBlockList.absolutePath}")
        }

        val enumNames: List<String> = registeredEnumPattern.findAll(modBlockList.readText())
            .map { match -> match.groupValues[1] }
            .toList()
        if (enumNames.isEmpty()) {
            throw GradleException("No block spec enums were found in ${modBlockList.absolutePath}.")
        }
        return enumNames
    }

    private fun loadEnumDefinitions(
        sourceDirectory: File,
        enumName: String
    ): List<BlockSpecDefinition> {
        val sourceFile: File = sourceDirectory.resolve("$enumName.java")
        if (!sourceFile.isFile) {
            throw GradleException("Generated block spec source is missing: ${sourceFile.absolutePath}")
        }

        val definitions: List<BlockSpecDefinition> = enumEntryPattern.findAll(sourceFile.readText())
            .map { match ->
                BlockSpecDefinition(
                    key = match.groupValues[2],
                    registryName = match.groupValues[3],
                    linkedTorchKey = match.groupValues[4].ifEmpty { null },
                    enumName = enumName
                )
            }
            .toList()
        if (definitions.isEmpty()) {
            throw GradleException("No block definitions were found in ${sourceFile.absolutePath}.")
        }
        return definitions
    }

    private fun verifyTableContent(
        path: String,
        expectation: LootTableExpectation,
        content: String
    ): Unit {
        val expectedItemId: String = "${modId.get()}:${expectation.droppedRegistryName}"
        if (!itemNamePattern(expectedItemId).containsMatchIn(content)) {
            throw GradleException("$path does not drop $expectedItemId.")
        }
        if (!survivesExplosionPattern.containsMatchIn(content)) {
            throw GradleException("$path is missing the survives_explosion condition.")
        }
        if (expectation.requiresUnsummonedCondition) {
            val expectedBlockId: String = "${modId.get()}:${expectation.registryName}"
            if (!blockStateConditionPattern(expectedBlockId).containsMatchIn(content)) {
                throw GradleException("$path must only drop when summoned is false.")
            }
        }
    }

    private fun lootTableDirectory(): String = "data/${modId.get()}/loot_table/blocks/"

    private fun lootTablePath(registryName: String): String =
        "${lootTableDirectory()}$registryName.json"

    private data class BlockSpecDefinition(
        val key: String,
        val registryName: String,
        val linkedTorchKey: String?,
        val enumName: String
    )

    private data class LootTableExpectation(
        val registryName: String,
        val droppedRegistryName: String,
        val requiresUnsummonedCondition: Boolean
    )

    private companion object {
        val registeredEnumPattern: Regex = Regex(
            """for \(var e : ([A-Za-z0-9_]+)\.values\(\)\) register\(e\);"""
        )

        val enumEntryPattern: Regex = Regex(
            """([A-Za-z0-9_]+)\("([^"]+)",\s*"([^"]+)"(?:,\s*"([^"]+)")?,\s*(?:true|false)\)"""
        )

        val survivesExplosionPattern: Regex = Regex(
            """"condition"\s*:\s*"minecraft:survives_explosion""""
        )

        fun blockStateConditionPattern(blockId: String): Regex = Regex(
            """"block"\s*:\s*"${Regex.escape(blockId)}"(?s:.*?)"condition"\s*:\s*"minecraft:block_state_property"(?s:.*?)"summoned"\s*:\s*"false""""
        )

        fun itemNamePattern(itemId: String): Regex = Regex(
            """"name"\s*:\s*"${Regex.escape(itemId)}""""
        )
    }
}
