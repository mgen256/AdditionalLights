package com.mgen256.conventions

import java.io.File
import java.io.Serializable
import org.gradle.api.Task
import org.gradle.api.specs.Spec

class PythonRequirementsMissingSpec(
    private val venvDir: File,
    private val requirementsFile: File,
) : Spec<Task>, Serializable {
    override fun isSatisfiedBy(element: Task): Boolean {
        return !arePythonRequirementsSatisfied(venvDir, requirementsFile)
    }

    private fun arePythonRequirementsSatisfied(venvDir: File, requirementsFile: File): Boolean {
        if (!venvDir.exists() || !requirementsFile.exists()) {
            return false
        }

        val sitePackages =
            listOf(File(venvDir, "Lib/site-packages")).firstOrNull { it.isDirectory }
                ?: File(venvDir, "lib")
                    .takeIf { it.isDirectory }
                    ?.listFiles()
                    ?.asSequence()
                    ?.filter { it.isDirectory && it.name.startsWith("python") }
                    ?.map { File(it, "site-packages") }
                    ?.firstOrNull { it.isDirectory }
                ?: return false

        val installedVersions = mutableMapOf<String, String>()
        sitePackages
            .listFiles { file -> file.isDirectory && file.name.endsWith(".dist-info") }
            ?.forEach { distInfo ->
                val withoutSuffix = distInfo.name.removeSuffix(".dist-info")
                val separatorIndex = withoutSuffix.lastIndexOf('-')
                if (separatorIndex <= 0 || separatorIndex >= withoutSuffix.length - 1) {
                    return@forEach
                }

                val rawName = withoutSuffix.substring(0, separatorIndex)
                val version = withoutSuffix.substring(separatorIndex + 1)
                val normalizedName = normalizePackageName(rawName)
                installedVersions.putIfAbsent(normalizedName, version)
            }

        return requirementsFile.readLines()
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .all { line ->
                val parts = line.split("==", limit = 2)
                if (parts.size != 2) {
                    return@all false
                }

                val normalizedName = normalizePackageName(parts[0].trim())
                val requiredVersion = parts[1].trim()
                installedVersions[normalizedName] == requiredVersion
            }
    }

    private fun normalizePackageName(name: String): String {
        return name.lowercase().replace("-", "_").replace(".", "_")
    }
}

