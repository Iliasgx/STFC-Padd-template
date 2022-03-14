pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
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
rootProject.name = "STFC Padd"
include(
    ":app",
    ":core",
    ":core-test",
    ":statistics",
    ":tracker",
    ":tracker:buildings",
    ":tracker:research"
)
