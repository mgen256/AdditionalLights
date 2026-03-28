import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.MapProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.api.file.ProjectLayout
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class BlockDataGeneratorTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val definitions: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val scriptFile: RegularFileProperty


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

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:OutputDirectories
    val outputDirs: MapProperty<String, Directory> = project.objects
        .mapProperty(String::class.java, Directory::class.java).convention(emptyMap())

    @get:Input
    val environment: MapProperty<String, String> = project.objects
        .mapProperty(String::class.java, String::class.java).convention(emptyMap())

    fun environment(name: String, value: Any) {
        environment.put(name, value.toString())
    }

    init {
        val venvProvider = project.provider {
            project.layout.projectDirectory.file(findVenvPython().absolutePath)
        }
        pythonExecutable.set(venvProvider)
        pythonPath.convention(venvProvider.map { it.asFile.absolutePath })
    }

    @get:Input
    val checkOnly: Property<Boolean> = project.objects.property(Boolean::class.java).convention(false)

    @get:Input
    val toolVersion: Property<String> = project.objects
        .property(String::class.java)
        .convention("0.1")

    @Option(option = "check-only", description = "Show diffs only")
    fun setCheckOnly(value: Boolean) {
        checkOnly.set(value)
    }

    @get:Inject
    protected abstract val workerExecutor: WorkerExecutor


    @get:Inject
    protected abstract val projectLayout: ProjectLayout

    @TaskAction
    fun generate() {
        val scriptFile = this.scriptFile.get()
        definitions.get()
        pythonExecutable.get()
        requirementsFile.get()
        outputDir.get()
        outputDirs.get()

        val pythonCmd = resolvePython()
        workerExecutor.classLoaderIsolation {
            classpath.setFrom(emptyList<Any>())
        }.submit(PythonWork::class.java) {
            python.set(pythonCmd)
            script.set(scriptFile)
            workDir.set(projectLayout.projectDirectory)
            environment.set(this@BlockDataGeneratorTask.environment.get())
            genDir.set(this@BlockDataGeneratorTask.outputDir)
            checkOnly.set(this@BlockDataGeneratorTask.checkOnly.getOrElse(false))
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
                val process = ProcessBuilder(
                    cmd,
                    "-c",
                    "import sys,shutil;print(shutil.which(\"$cmd\") or \"\")"
                )
                    .redirectErrorStream(true)
                    .start()
                val output = process.inputStream.bufferedReader().readText().trim()
                val exitCode = process.waitFor()
                if (exitCode == 0) {
                    if (output.isNotEmpty()) {
                        return output
                    }
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
        val script: RegularFileProperty
        val workDir: DirectoryProperty
        val checkOnly: Property<Boolean>
        val environment: MapProperty<String, String>
        val genDir: DirectoryProperty
    }

    abstract class PythonWork : WorkAction<PythonParams> {
        override fun execute() {
            val command = mutableListOf(
                parameters.python.get(),
                parameters.script.asFile.get().absolutePath
            )
            command += listOf("--gen-dir", parameters.genDir.get().asFile.absolutePath)
            if (parameters.checkOnly.getOrElse(false)) {
                command += "--check-only"
            }

            val pb = ProcessBuilder(command)
                .directory(parameters.workDir.get().asFile)
                .inheritIO()
            pb.environment().putAll(parameters.environment.get())

            val exit = pb
                .start()
                .waitFor()
            if (exit != 0) {
                throw GradleException("datagen failed with exit code $exit")
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
