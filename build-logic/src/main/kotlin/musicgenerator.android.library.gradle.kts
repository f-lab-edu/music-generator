import com.simuel.musicgenerator.configureCoroutineAndroid
import com.simuel.musicgenerator.configureHiltAndroid
import com.simuel.musicgenerator.configureJacocoAndroid
import com.simuel.musicgenerator.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
configureJacocoAndroid(android)
