plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
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
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Rating bar
    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")
}