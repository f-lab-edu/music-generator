import com.simuel.musicgenerator.setNamespace

plugins {
    id("musicgenerator.android.library")
}

android {
    setNamespace("core.database")
}

dependencies {
    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    
    // Coroutines
    implementation(libs.bundles.coroutines)
    
    // Serialization for TypeConverter
    implementation(libs.kotlinx.serialization.json)
    
    // Core modules
    implementation(projects.core.data)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.room.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
