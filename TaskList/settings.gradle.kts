pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "7.1.0-beta02"
        id("org.jetbrains.kotlin.android") version "1.5.31"
        id("org.jetbrains.kotlin.kapt") version "1.5.31"
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
