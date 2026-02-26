import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.gms.google-services")
}

val apikeyProperties = Properties()
val apikeyPropertiesFile = rootProject.file("apikey.properties")
if (apikeyPropertiesFile.exists()) {
    apikeyProperties.load(apikeyPropertiesFile.inputStream())
}
val googleApiKey = apikeyProperties.getProperty("GOOGLE_API_KEY") ?: ""

android {
    namespace = "com.example.eventorias"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.eventorias"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.eventorias.TestRunner"
        
        buildConfigField("String", "GOOGLE_API_KEY", googleApiKey)
    }

    buildTypes {
        debug {
            enableAndroidTestCoverage = true
        }
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-kt"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.compose.material.ripple)
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Firebase
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.messaging)

    //Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.koin.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}