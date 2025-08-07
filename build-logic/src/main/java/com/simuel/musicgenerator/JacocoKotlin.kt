package com.simuel.musicgenerator

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

internal fun Project.configureJacocoKotlin() {
    plugins.apply("jacoco")

    configure<JacocoPluginExtension> {
        toolVersion = extensions.libs.findVersion("jacoco").get().toString()
    }

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    if (!tasks.names.contains("jacocoTestReport")) {
        tasks.register<JacocoReport>("jacocoTestReport") {
            dependsOn("test")

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            classDirectories.setFrom(
                fileTree("$buildDir/classes/kotlin/main") {
                    exclude(
                        "**/R.class",
                        "**/R$*.class",
                        "**/BuildConfig.*",
                        "**/Manifest*.*",
                        "**/*Test*.*",
                        "**/*\$Lambda$*.*",
                        "**/*\$inlined$*.*",
                        "**/di/*Module*.*",
                        "**/*Module*.*",
                        "**/*Dagger*.*",
                        "**/*Hilt*.*",
                        "**/*MembersInjector*.*",
                        "**/*_Provide*Factory*.*",
                        "**/*_Factory*.*"
                    )
                }
            )

            sourceDirectories.setFrom(
                "$projectDir/src/main/kotlin",
                "$projectDir/src/main/java"
            )

            executionData.setFrom(
                fileTree(buildDir) {
                    include("**/*.exec", "**/*.ec")
                }
            )
        }
    }
}