// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.google.com") // ✅ necesario para ML Kit
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.google.com") // ✅ aquí también
        gradlePluginPortal()
    }
}

rootProject.name = "InventariadosApp"
include(":app")
