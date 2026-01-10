package com.hatem.noureddine.tictactoe.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            extensions.configure<JacocoPluginExtension> {
                toolVersion = "0.8.11"
            }

            tasks.withType<Test> {
                configure<org.gradle.testing.jacoco.plugins.JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }

            // We need to wait for the Android plugin to be applied to register the task properly
            // involving android variants, but since this is specific to `app` and debug variant,
            // we can stick to the previous simple registration or improve it.
            // The previous code was:

            val exclusions =
                listOf(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "**/data/models/*",
                    "**/*Hilt*.*",
                    "hilt_aggregated_deps/**",
                    "**/*_Factory.class",
                    "**/*_MembersInjector.class",
                )

            // Register task for Android modules
            if (pluginManager.hasPlugin("com.android.library") || pluginManager.hasPlugin("com.android.application")) {
                tasks.register<JacocoReport>("testDebugUnitTestCoverage") {
                    dependsOn("testDebugUnitTest")
                    group = "Reporting"
                    description = "Generate Jacoco coverage reports for the debug build."

                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                    }

                    val debugTree =
                        fileTree(
                            project.layout.buildDirectory
                                .dir("tmp/kotlin-classes/debug")
                                .get()
                                .asFile,
                        ) {
                            exclude(exclusions)
                        }
                    val mainSrc = project.layout.projectDirectory.dir("src/main/java")

                    sourceDirectories.setFrom(files(mainSrc))
                    classDirectories.setFrom(files(debugTree))
                    executionData.setFrom(
                        files(
                            project.layout.buildDirectory
                                .file("jacoco/testDebugUnitTest.exec")
                                .get()
                                .asFile,
                            fileTree(
                                project.layout.buildDirectory
                                    .dir("outputs/code_coverage/debugAndroidTest/connected/")
                                    .get()
                                    .asFile,
                            ) {
                                include("*.ec")
                            },
                        ),
                    )
                }

                tasks.register<org.gradle.testing.jacoco.tasks.JacocoCoverageVerification>("verifyJacocoCoverage") {
                    dependsOn("testDebugUnitTestCoverage")
                    group = "Reporting"
                    description = "Verify Jacoco coverage thresholds."

                    val debugTree =
                        fileTree(
                            project.layout.buildDirectory
                                .dir("tmp/kotlin-classes/debug")
                                .get()
                                .asFile,
                        ) {
                            exclude(exclusions)
                        }
                    val mainSrc = project.layout.projectDirectory.dir("src/main/java")

                    sourceDirectories.setFrom(files(mainSrc))
                    classDirectories.setFrom(files(debugTree))
                    executionData.setFrom(
                        files(
                            project.layout.buildDirectory
                                .file("jacoco/testDebugUnitTest.exec")
                                .get()
                                .asFile,
                        ),
                    )

                    violationRules {
                        rule {
                            limit {
                                minimum = 0.2.toBigDecimal() // 80% coverage required
                            }
                        }
                    }
                }
            } else {
                // Register task for JVM modules
                tasks.register<JacocoReport>("testCoverage") {
                    dependsOn("test")
                    group = "Reporting"
                    description = "Generate Jacoco coverage reports."

                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                    }

                    val classTree =
                        fileTree(
                            project.layout.buildDirectory
                                .dir("classes/kotlin/main")
                                .get()
                                .asFile,
                        )
                    val mainSrc = project.layout.projectDirectory.dir("src/main/kotlin")

                    sourceDirectories.setFrom(files(mainSrc))
                    classDirectories.setFrom(files(classTree))
                    executionData.setFrom(
                        files(
                            project.layout.buildDirectory
                                .file("jacoco/test.exec")
                                .get()
                                .asFile,
                        ),
                    )
                }
            }
        }
    }
}
