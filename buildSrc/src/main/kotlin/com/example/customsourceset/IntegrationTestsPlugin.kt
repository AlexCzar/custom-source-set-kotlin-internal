package com.example.customsourceset

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

class IntegrationTestsPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        val testImplementation by configurations
        val testRuntimeOnly by configurations
        val testRuntime by configurations

        configurations.create("testIImplementation") {
            extendsFrom(testImplementation)
        }

        configurations.create("testIRuntimeOnly") {
            extendsFrom(testRuntimeOnly)
        }

        configurations.create("testIRuntime") {
            extendsFrom(testRuntime)
        }

        val sourceSets = the<SourceSetContainer>()
        val testI: SourceSet by sourceSets.creating {
            compileClasspath += sourceSets["main"].output + sourceSets["test"].output
            runtimeClasspath += sourceSets["main"].output + sourceSets["test"].output
        }

        with(tasks) {
            val test by getting
            @Suppress("NAME_SHADOWING")
            val testI by registering(Test::class) {
                group = "verification"
                testClassesDirs = testI.output.classesDirs
                classpath = testI.runtimeClasspath
                mustRunAfter(test)
            }

            val check by getting
            check.dependsOn(testI)
        }
    }
}
