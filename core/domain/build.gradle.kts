import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    id("musicgenerator.kotlin.library")
}

dependencies {
    // Dependency Injection
    implementation(libs.javax.inject)
    
    // Coroutines
    implementation(libs.coroutines.core)
    
    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}

// Jacoco test report 설정
tasks.named<JacocoReport>("jacocoTestReport") {
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/domain/model/**",
                    "**/model/**"
                )
            }
        })
    )
}