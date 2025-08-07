import com.simuel.musicgenerator.setNamespace


plugins {
    id("musicgenerator.android.library")
    id("musicgenerator.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.data")
}

dependencies {
    // Network
    implementation(libs.kotlinx.serialization.json)
    
    // Module dependencies
    implementation(project(":core:domain"))

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
}
