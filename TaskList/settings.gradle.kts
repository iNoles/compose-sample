pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "7.1.0-beta01"
        id("org.jetbrains.kotlin.android") version "1.5.31"
        id("org.jetbrains.kotlin.kapt") version "1.5.31"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Task List"
include(":app")
