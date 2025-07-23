plugins {
    // Apply the Android Application plugin to the app module
    alias(libs.plugins.android.application) apply false
    // Apply the Kotlin Android plugin to Kotlin modules
    alias(libs.plugins.kotlin.android) apply false
    // Apply the Hilt plugin for dependency injection
    alias(libs.plugins.dagger.hilt.android) apply false
}