// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("room_version", "2.5.0")
    }
}

plugins {
    alias(libs.plugins.androidApplication) version "8.4.2" apply false
    alias(libs.plugins.androidLibrary) version "8.4.2" apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) version "1.9.0" apply false
    alias(libs.plugins.ksp) version "1.9.21-1.0.15" apply false
}