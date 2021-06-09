plugins {
    java
    idea
    `maven-publish`
    id("com.diffplug.spotless") version Versions.gradleSpotlessPlugin
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
    apply(plugin = "com.diffplug.spotless")

    apply(from = "${project.rootDir}/gradle/dependencyUpdates.gradle.kts")

    // FIXME replace with your company's package
    group = "com.tailrocks.example"

    idea {
        module {
            isDownloadJavadoc = false
            isDownloadSources = false
        }
    }

    spotless {
        java {
            licenseHeaderFile("$rootDir/gradle/licenseHeader.txt")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlin {
            licenseHeaderFile("$rootDir/gradle/licenseHeader.txt")
        }
        kotlinGradle {
            ktlint()
        }
    }

    tasks.withType<JavaCompile> {
        if (javaVersion >= 9) {
            options.release.set(javaVersion)
        }
    }
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

    plugins.withId("maven-publish") {
        publishing {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])
                    versionMapping {
                        allVariants {
                            fromResolutionResult()
                        }
                    }
                }
            }
        }
    }
}
