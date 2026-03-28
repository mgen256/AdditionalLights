package com.mgen256.conventions

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.util.zip.ZipFile

@CacheableTask
abstract class VerifyDuplicateResourcesTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val jarFile: RegularFileProperty

    @TaskAction
    fun verify() {
        val file = jarFile.get().asFile
        val duplicates = mutableListOf<String>()
        val seen = mutableSetOf<String>()
        ZipFile(file).use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val name = entry.name
                if (!seen.add(name)) duplicates += name
            }
        }
        if (duplicates.isNotEmpty()) {
            throw GradleException("Detected duplicate entries in ${file.name}: ${duplicates.joinToString()}")
        }
    }
}
