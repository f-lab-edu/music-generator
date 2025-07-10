plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}
dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "musicgenerator.android.hilt"
            implementationClass = "com.simuel.musicgenerator.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "musicgenerator.kotlin.hilt"
            implementationClass = "com.simuel.musicgenerator.HiltKotlinPlugin"
        }
    }
}
