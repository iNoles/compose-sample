pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "7.2.0-alpha07"
        id("org.jetbrains.kotlin.android") version "1.6.10"
        id("org.jetbrains.kotlin.kapt") version "1.6.10"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/snapshots/latest/artifacts/repository")
    }
}
rootProject.name = "Task List"
include(":app")
