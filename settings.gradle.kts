pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io")}
    }
}

rootProject.name = "NumSum"
include(":app")
include(":feature:home")
include(":feature:calculator")
include(":core:designsystem")
include(":core:database")
include(":core:constant")
include(":core:data")
include(":core:datastore")
include(":core:ui")
include(":core:model")
include(":core:firebase")
include(":feature:onboard")
include(":core:workmanager")
include(":core:network")
include(":core:common")
