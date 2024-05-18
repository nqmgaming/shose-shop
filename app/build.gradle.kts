plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksps)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nqmgaming.shoseshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nqmgaming.shoseshop"
        minSdk = 27
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.easyvalidation.core)

    // Shows Toasts by default for every validation error
    implementation (libs.easyvalidation.toast)

    // Gson
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.logging.interceptor)

    // Jwt Decoder
    implementation (libs.jwtdecode)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    //popup dialog
    implementation (libs.popup.dialog)

    // Avatar Image View
    implementation(libs.avvylib)

    // Ted Permission
    implementation(libs.tedpermission.coroutine)

    // Image Picker
    implementation (libs.imagepicker)

    // Dot Indicator
    implementation(libs.dotsindicator)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)

    implementation(libs.androidx.work.runtime.ktx)

    // Rating bar
    implementation(libs.simpleratingbar)

    // Bottom sheet
    implementation(libs.input)
}