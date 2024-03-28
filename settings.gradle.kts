@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://www.jitpack.io")
        google()
        mavenCentral()
    }
}

rootProject.name = "WanAndroidV3"
include(":app")
include(":mod_web")
include(":lib_common")
include(":lib_framework")
include(":lib_coil")
include(":lib_network")
include(":lib_utils")
