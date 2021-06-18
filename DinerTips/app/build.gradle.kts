plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.diffplug.spotless") version "5.12.5"
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.jonathansteele.dinertips"
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["composeVersion"] as String
    }
}
spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt")
        targetExclude("bin/**/*.kt")
        ktlint("0.41.0")
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["composeVersion"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["composeVersion"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["composeVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-beta02")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha08")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["composeVersion"]}")
}