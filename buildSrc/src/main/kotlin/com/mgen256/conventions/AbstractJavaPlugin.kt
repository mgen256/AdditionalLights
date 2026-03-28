package com.mgen256.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.register
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.kotlin.dsl.named
import com.mgen256.conventions.VerifyDuplicateResourcesTask

open class AbstractJavaPlugin(
    private val modAware: Boolean
) : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.withPlugin("java") {
            val java = target.extensions.getByType(JavaPluginExtension::class.java)
            java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))
            java.withSourcesJar()

            target.tasks.withType(JavaCompile::class.java).configureEach {
                options.release.set(25)
                options.encoding = "UTF-8"
            }

            target.tasks.withType(Test::class.java).configureEach {
                useJUnitPlatform()
            }

            target.tasks.withType(Jar::class.java).configureEach {
                duplicatesStrategy = DuplicatesStrategy.FAIL
            }

            val verifyTask = target.tasks.register<VerifyDuplicateResourcesTask>("verifyDuplicateResources")

            target.afterEvaluate {
                val jarTaskProvider = target.tasks.named<Jar>("jar")
                verifyTask.configure {
                    jarFile.set(jarTaskProvider.flatMap { it.archiveFile })
                }
                jarTaskProvider.configure {
                    finalizedBy(verifyTask)
                }
            }

            if (modAware) {
                target.tasks.named<ProcessResources>("processResources").configure {
                    dependsOn(target.rootProject.tasks.named("generateBlockData"))
                }
                target.tasks.named<Jar>("sourcesJar").configure {
                    dependsOn(target.rootProject.tasks.named("generateBlockData"))
                }
            }
        }
    }
}
