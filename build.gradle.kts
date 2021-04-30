plugins {
    java
    idea
    `maven-publish`
    // TODO not working with https://docs.gradle.org/current/userguide/configuration_cache.html
    /*
    id("com.diffplug.spotless") version Versions.gradleSpotlessPlugin
     */
    id("com.gorylenko.gradle-git-properties") version Versions.gradleGitPropertiesPlugin apply false
}

val javaVersion = 16

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

allprojects {
    apply(plugin = "idea")
    // TODO not working with https://docs.gradle.org/current/userguide/configuration_cache.html
    /*
    apply(plugin = "com.diffplug.spotless")
    */

    apply(from = "${project.rootDir}/gradle/dependencyUpdates.gradle.kts")

    group = "com.tailrocks.api"

    idea {
        module {
            isDownloadJavadoc = false
            isDownloadSources = false
        }
    }

    // TODO not working with https://docs.gradle.org/current/userguide/configuration_cache.html
    /*
    spotless {
        java {
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlinGradle {
            ktlint()
        }
    }
    */
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }

        withJavadocJar()
        withSourcesJar()
    }

    dependencies {
        // SpotBugs
        implementation("com.github.spotbugs:spotbugs-annotations:${Versions.spotbugs}")
    }
}
