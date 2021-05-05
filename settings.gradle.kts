pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        // uncomment if you need to use snapshot versions
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

// FIXME replace tailrocks-example with microservice name, for example tailrocks-payment
rootProject.name = "tailrocks-example"

// FIXME replace tailrocks-example with microservice prefix and don't forget rename folders
include(
    // libraries
    ":tailrocks-example-api",
    ":tailrocks-example-api-client",
    ":tailrocks-example-grpc-interface",

    // apps
    ":tailrocks-example-api-app",

    // tests
    ":tailrocks-example-api-test"
)
