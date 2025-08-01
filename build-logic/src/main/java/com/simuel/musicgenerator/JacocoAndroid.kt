package com.simuel.musicgenerator

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

internal fun Project.configureJacocoAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    plugins.apply("jacoco")

    configure<JacocoPluginExtension> {
        toolVersion = extensions.libs.findVersion("jacoco").get().toString()
    }

    commonExtension.apply {
        buildTypes {
            getByName("debug") {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }

        testOptions {
            unitTests.all {
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    val androidComponents = extensions.findByType(AndroidComponentsExtension::class.java)
        ?: throw IllegalStateException("Android components extension not found")
    
    androidComponents.onVariants { variant ->
        val testTaskName = "test${variant.name.replaceFirstChar { it.uppercaseChar() }}UnitTest"

        tasks.register<JacocoReport>("jacoco${variant.name.replaceFirstChar { it.uppercaseChar() }}TestReport") {
            dependsOn(testTaskName)

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            classDirectories.setFrom(
                fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                    exclude(
                        "**/R.class",
                        "**/R$*.class",
                        "**/BuildConfig.*",
                        "**/Manifest*.*",
                        "**/*Test*.*",
                        "android/**/*.*",
                        "**/*\$Lambda$*.*",
                        "**/*\$inlined$*.*",
                        "**/di/*Module*.*",
                        "**/*Module*.*",
                        "**/*Dagger*.*",
                        "**/*Hilt*.*",
                        "**/*MembersInjector*.*",
                        "**/*_Provide*Factory*.*",
                        "**/*_Factory*.*",
                        "**/*\$ViewInjector*.*",
                        "**/*\$ViewBinder*.*",
                        "**/Manifest_*.*",
                        "**/*DatabaseImpl*.*",
                        "**/*DatabaseImpl\$*.*",
                        "**/*Dao_Impl*.*",
                        "**/*Dao_Impl\$*.*",
                        "**/db/*Database.*",
                        "**/db/*Database\$*.*",
                        "**/db/**"
                    )
                }
            )

            sourceDirectories.setFrom(
                "$projectDir/src/main/java",
                "$projectDir/src/main/kotlin"
            )

            executionData.setFrom(
                fileTree(buildDir) {
                    include("**/*.exec", "**/*.ec")
                }
            )
        }
    }
}