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
    }
}

rootProject.name = "NumSum"
include(":app")
include(":feature:temporary")
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
