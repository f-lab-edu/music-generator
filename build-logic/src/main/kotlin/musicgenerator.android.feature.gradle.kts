import com.simuel.musicgenerator.configureHiltAndroid
import com.simuel.musicgenerator.libs


plugins {
    id("musicgenerator.android.library")
    id("musicgenerator.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
configureHiltAndroid()

dependencies {
    val libs = project.extensions.libs
    "implementation"(libs.findLibrary("hilt-navigation-compose").get())
    "implementation"(libs.findLibrary("androidx-navigation-compose").get())
    "androidTestImplementation"(libs.findLibrary("androidx-navigation-testing").get())
    "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
    "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
}
