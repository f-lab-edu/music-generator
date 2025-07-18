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

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}
