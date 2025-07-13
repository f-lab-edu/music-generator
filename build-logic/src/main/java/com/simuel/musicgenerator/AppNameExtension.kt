package com.simuel.musicgenerator

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.simuel.musicgenerator.$name"
    }
}
