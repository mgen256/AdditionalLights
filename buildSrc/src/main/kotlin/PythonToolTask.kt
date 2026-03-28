package com.mgen256.conventions

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import org.gradle.api.logging.Logging
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class PythonToolTask : DefaultTask() {
    @get:Input
    abstract val pythonPath: Property<String>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val pythonExecutable: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val requirementsFile: RegularFileProperty = project.objects.fileProperty().convention(
        project.rootProject.layout.projectDirectory.file("requirements.txt")
    )

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val script: RegularFileProperty

    @get:Input
    @get:Optional
    val module: Property<String> = project.objects.property(String::class.java)

    @get:Input
    val args: ListProperty<String> = project.objects.listProperty(String::class.java).convention(emptyList())

    @get:Input
    val env: MapProperty<String, String> = project.objects.mapProperty(String::class.java, String::class.java).convention(emptyMap())

    init {
        val venvProvider = project.provider {
            project.layout.projectDirectory.file(findVenvPython().absolutePath)
        }
        pythonExecutable.set(venvProvider)
        pythonPath.convention(venvProvider.map { it.asFile.absolutePath })
    }

    @get:Input
    val toolVersion: Property<String> = project.objects
        .property(String::class.java)
        .convention("0.1")

    @get:Internal
    val workingDir: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.projectDirectory)

    @get:Inject
    protected abstract val workerExecutor: WorkerExecutor

    @get:Inject
    protected abstract val projectLayout: ProjectLayout

    @TaskAction
    fun run() {
        pythonExecutable.get()
        requirementsFile.get()
        val pythonCmd = resolvePython()
        workerExecutor.classLoaderIsolation {
            classpath.setFrom(emptyList<Any>())
        }.submit(PythonWork::class.java) {
            python.set(pythonCmd)
            scriptFile.set(this@PythonToolTask.script.orNull)
            moduleName.set(this@PythonToolTask.module.orNull)
            arguments.set(args.get())
            workDir.set(workingDir.get())
            environment.set(env.get())
        }
    }

private fun resolvePython(): String {
        val venvPython = pythonExecutable.get().asFile
        if (venvPython.exists()) {
            return venvPython.absolutePath
        }

        throw GradleException(
            ".venv Python was not found. Run `python -m venv .venv` and install the dependencies."
        )
    }

    private fun findPython(): String {
        val candidates = listOf(
            "python", "python3", "py", "py.exe"
        )
        for (cmd in candidates) {
            try {
                val process = ProcessBuilder(cmd, "-c", "import sys,shutil;print(shutil.which(\"$cmd\") or \"\")")
                    .redirectErrorStream(true)
                    .start()
                val output = process.inputStream.bufferedReader().readText().trim()
                val exitCode = process.waitFor()
                if (exitCode == 0) {
                    if (output.isNotEmpty()) return output
                    return cmd
                }
            } catch (_: Exception) {
            }
        }
        throw GradleException(
            "No Python executable was found. Install `python` and configure PATH."
        )
    }

    interface PythonParams : WorkParameters {
        val python: Property<String>
        val scriptFile: RegularFileProperty
        val moduleName: Property<String>
        val arguments: ListProperty<String>
        val workDir: DirectoryProperty
        val environment: MapProperty<String, String>
    }

    abstract class PythonWork : WorkAction<PythonParams> {
        override fun execute() {
            val logger = Logging.getLogger(PythonWork::class.java)
            val python = parameters.python.get()
            val command = mutableListOf<String>()
            command += python
            when {
                parameters.moduleName.isPresent -> {
                    command += listOf("-m", parameters.moduleName.get())
                }
                parameters.scriptFile.isPresent -> {
                    command += parameters.scriptFile.get().asFile.absolutePath
                }
                else -> throw GradleException("module or script must be specified")
            }
            command.addAll(parameters.arguments.get())

            val pb = ProcessBuilder(command)
                .directory(parameters.workDir.asFile.get())
                .redirectErrorStream(true)
            pb.environment().putAll(parameters.environment.get())

            logger.lifecycle(">> PythonToolTask exec: ${command.joinToString(" ")}")

            val proc = pb.start()
            val output = proc.inputStream.bufferedReader().readText()
            val exit = proc.waitFor()
            if (exit != 0) {
                throw GradleException(
                    buildString {
                        appendLine("Python tool failed with exit code $exit")
                        append(output)
                    }
                )
            }
            if (output.isNotBlank()) {
                logger.lifecycle(output.trimEnd())
            }
        }
    }

    private fun findVenvPython(): File {
        val suffix = if (System.getProperty("os.name").lowercase().contains("windows"))
            "Scripts/python.exe" else "bin/python"

        val repoRoot = project.rootProject.layout.projectDirectory.asFile

        val candidates = mutableListOf(File(repoRoot, ".venv/$suffix"))
        val dotGit = File(repoRoot, ".git")
        if (dotGit.isFile) {
            val path = dotGit.readText().removePrefix("gitdir:").trim()
            val worktreeGit = if (File(path).isAbsolute) File(path) else File(repoRoot, path)
            val mainRepoDir = worktreeGit.parentFile?.parentFile?.parentFile
            if (mainRepoDir != null && mainRepoDir != repoRoot) {
                candidates += File(mainRepoDir, ".venv/$suffix")
            }
        }

        val existing = candidates.firstOrNull { it.exists() }
        if (existing != null) return existing
        return candidates.first()
    }
}
