plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.problem_catalog"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.problem_catalog"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Для JSON

    // Logging
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.livedata)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)

    // Dagger Hilt
    implementation(libs.hilt.android)

    // Lombok
    compileOnly(libs.lombok)

}