plugins {
    id("musicgenerator.kotlin.library")
}

dependencies {
    // Coroutines
    implementation(libs.coroutines.core)
    
    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}