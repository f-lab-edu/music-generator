import com.simuel.musicgenerator.configureHiltAndroid
import com.simuel.musicgenerator.configureJacocoAndroid
import com.simuel.musicgenerator.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureJacocoAndroid(android)