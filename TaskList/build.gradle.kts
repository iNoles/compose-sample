// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeVersion by extra("1.1.0-alpha02")
    val roomVersion by extra("2.4.0-alpha04")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

plugins {
    id("com.diffplug.spotless") version "5.14.3"
}

subprojects {
    apply(plugin="com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint("0.42.1")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
        kotlinGradle {
            target("*.gradle.kts") // default target for kotlinGradle
            ktlint() // or ktfmt() or prettier()
        }
    }
}
