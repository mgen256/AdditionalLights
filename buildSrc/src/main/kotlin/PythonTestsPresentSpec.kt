package com.mgen256.conventions

import java.io.File
import java.io.Serializable
import org.gradle.api.Task
import org.gradle.api.specs.Spec

class PythonTestsPresentSpec(
    private val testsDir: File,
) : Spec<Task>, Serializable {
    override fun isSatisfiedBy(element: Task): Boolean {
        return hasPythonTests(testsDir)
    }

    companion object {
        fun hasPythonTests(testsDir: File): Boolean {
            return testsDir.isDirectory && testsDir.walkTopDown().any {
                it.isFile && it.extension == "py"
            }
        }
    }
}
