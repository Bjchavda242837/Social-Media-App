import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.socialmedia"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.socialmedia"
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.kotlinStdlib)
    api(libs.coroutinesCore)
    api(libs.coroutinesAndroid)
    implementation(libs.constraintLayout)
    implementation(libs.material)
    implementation(libs.coroutinesPlayServices)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseFirestore)
    implementation(libs.firebaseAuthKtx)
    implementation(libs.firebaseUiFirestore)
    implementation(libs.playServicesAuth)

    implementation(libs.glide)
    implementation(libs.activity)
    annotationProcessor(libs.glideCompiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.coreTesting)
    androidTestImplementation(libs.androidxJunit)

    implementation(libs.credentials)
    implementation(libs.credentialsPlayServicesAuth)
    implementation(libs.googleid)


}