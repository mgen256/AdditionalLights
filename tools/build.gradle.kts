plugins {
    base
}

import com.mgen256.conventions.PythonRequirementsMissingSpec
import com.mgen256.conventions.PythonTestsPresentSpec

val toolsTestsDir = rootProject.layout.projectDirectory.dir("tools/tests").asFile
val hasToolsTests = PythonTestsPresentSpec.hasPythonTests(toolsTestsDir)

tasks.register<com.mgen256.conventions.PythonToolTask>("installPythonDeps") {
    group = org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Install Python requirements"
    workingDir.set(rootProject.layout.projectDirectory)
    module.set("pip")
    args.set(listOf("install", "-r", "requirements.txt"))
    env.put("PIP_CACHE_DIR", System.getProperty("user.home") + "/.cache/pip")

    val venvDir = rootProject.layout.projectDirectory.dir(".venv").asFile
    val requirementsFile = rootProject.layout.projectDirectory.file("requirements.txt").asFile
    onlyIf(PythonRequirementsMissingSpec(venvDir, requirementsFile))
}

tasks.register<com.mgen256.conventions.PythonToolTask>("test") {
    group = org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Run pytest for the tools test suite"
    workingDir.set(rootProject.layout.projectDirectory)
    module.set("pytest")
    args.set(listOf("tools/tests"))
    if (hasToolsTests) {
        dependsOn(tasks.named("installPythonDeps"))
    }
    onlyIf(PythonTestsPresentSpec(toolsTestsDir))
}
