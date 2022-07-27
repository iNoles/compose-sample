plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

val composeVersion = "1.2.0-rc03"

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.jonathansteele.tasklist"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
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
        kotlinCompilerExtensionVersion = composeVersion
    }
    namespace = "com.jonathansteele.tasklist"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("androidx.core:core-ktx:1.8.0")

    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.0")

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")

    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("com.google.accompanist:accompanist-pager-indicators:0.24.6-alpha")

    val glanceVersion = "1.0.0-SNAPSHOT"
    implementation("androidx.glance:glance-appwidget:$glanceVersion")
    implementation("androidx.glance:glance:$glanceVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}
