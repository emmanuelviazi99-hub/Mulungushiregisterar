plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "zm.ac.mulungushi.registrar"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "zm.ac.mulungushi.registrar"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.recyclerview)

    // ViewModel / LiveData (viewmodel package)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Local storage (data/local): Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Remote (data/remote): Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)

    // Background sync (data/sync): WorkManager
    implementation(libs.work.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}