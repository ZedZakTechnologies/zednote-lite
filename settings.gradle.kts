pluginManagement {
    repositories {
        // Ensure the Gradle Plugin Portal is searched first for plugin artifacts
        gradlePluginPortal()

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        // JetBrains Compose plugin/artifacts are sometimes published to this Space repo
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        // Kotlin EAP/dev builds and some Kotlin plugins may be hosted here
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")

        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ZedNoteLite"
include(":app")
