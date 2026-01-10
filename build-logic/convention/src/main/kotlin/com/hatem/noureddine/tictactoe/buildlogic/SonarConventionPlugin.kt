package com.hatem.noureddine.tictactoe.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

class SonarConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.sonarqube")

            extensions.configure<SonarExtension> {
                properties {
                    configureSonarProperties(target)
                }
            }
        }
    }

    private fun org.sonarqube.gradle.SonarProperties.configureSonarProperties(target: Project) {
        // Try to get keys from Environment Variables (CI) or local.properties (Local)
        val localProperties = java.util.Properties()
        val localPropertiesFile = target.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        val projectKey =
            System.getenv("SONAR_PROJECT_KEY")
                ?: localProperties.getProperty("sonar.projectKey")
                ?: "2026-DEV2-007-Hatem-NOUREDDINE_tictactoe"

        val organization =
            System.getenv("SONAR_ORGANIZATION_KEY")
                ?: localProperties.getProperty("sonar.organization")
                ?: "2026-dev2-007-hatem-noureddine"

        property("sonar.projectKey", projectKey)
        property("sonar.organization", organization)
        property("sonar.host.url", "https://sonarcloud.io")

        // Note: SONAR_TOKEN is automatically picked up from environment variable "SONAR_TOKEN"
        // by the SonarQube plugin. To support local execution with token in local.properties:
        val token =
            System.getenv("SONAR_TOKEN")
                ?: localProperties.getProperty("sonar.token")

        if (token != null) {
            property("sonar.login", token)
        }

        // Specify Android variant for analysis
        property("sonar.android.variant", "debug")

        // External Analyzers - Detekt
        property(
            "sonar.kotlin.detekt.reportPaths",
            "${target.rootProject.layout.buildDirectory.get()}/reports/detekt/detekt.xml",
        )

        // External Analyzers - Android Lint
        property(
            "sonar.androidLint.reportPaths",
            listOf("app", "data").joinToString(",") { module ->
                "${target.rootProject.projectDir}/$module/build/reports/lint-results-debug.xml"
            },
        )

        // External Analyzers - KtLint
        property(
            "sonar.kotlin.ktlint.reportPaths",
            listOf("app", "domain", "data")
                .flatMap { module ->
                    val absolutePath = "${target.rootProject.projectDir}/$module/build/reports/ktlint"
                    listOf(
                        "$absolutePath/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.xml",
                        "$absolutePath/ktlintTestSourceSetCheck/ktlintTestSourceSetCheck.xml",
                    )
                }.joinToString(","),
        )

        // Coverage - JaCoCo
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            listOf("app", "data", "domain")
                .flatMap { module ->
                    val absolutePath = "${target.rootProject.projectDir}/$module/build/reports/jacoco"
                    listOf(
                        "$absolutePath/testDebugUnitTestCoverage/testDebugUnitTestCoverage.xml",
                        "$absolutePath/testCoverage/testCoverage.xml",
                    )
                }.joinToString(","),
        )
    }
}
