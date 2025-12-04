buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0") // Adjust based on your Studio's suggestion
        classpath("com.google.gms:google-services:4.4.2") // Firebase
        classpath("com.android.tools.build:gradle:8.1.1") // Use a compatible Gradle plugin version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // Add Kotlin plugin
        classpath("com.google.gms:google-services:4.3.15")
    }
}
