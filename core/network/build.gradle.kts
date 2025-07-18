import com.simuel.musicgenerator.setNamespace
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("musicgenerator.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.network")
    buildFeatures {
        buildConfig = true
    }
    
    defaultConfig {
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))
        }
        
        buildConfigField(
            "String",
            "LOUDLY_API_KEY",
            "\"${properties.getProperty("LOUDLY_API_KEY", "")}\""
        )
    }
}

dependencies {
    implementation(projects.core.data)
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
