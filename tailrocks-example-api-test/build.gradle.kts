plugins {
    id("com.github.johnrengelman.shadow") version Versions.gradleShadowPlugin
    id("io.micronaut.application") version Versions.gradleMicronautPlugin
    kotlin("jvm") version Versions.kotlin
    kotlin("kapt") version Versions.kotlin
    kotlin("plugin.allopen") version Versions.kotlin
}

micronaut {
    version(Versions.micronaut)
    runtime("netty")
    testRuntime("junit5")
    enableNativeImage(false)
    processing {
        incremental(true)
        // FIXME replace package name
        annotations("com.tailrocks.example.api.*")
    }
}

dependencies {
    // subprojects
    implementation(project(":tailrocks-example-api"))
    implementation(project(":tailrocks-example-api-client"))

    // Micronaut
    implementation(enforcedPlatform("io.micronaut.test:micronaut-test-bom:2.3.4-SNAPSHOT")) // TODO remove later
    implementation("io.micronaut.grpc:micronaut-grpc-client-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    kapt(enforcedPlatform("io.micronaut:micronaut-bom:${Versions.micronaut}"))
    kapt("io.micronaut:micronaut-inject-java")
    kaptTest(enforcedPlatform("io.micronaut:micronaut-bom:${Versions.micronaut}"))
    kaptTest("io.micronaut:micronaut-inject-java")

    // PGV
    implementation("io.envoyproxy.protoc-gen-validate:pgv-java-grpc:${Versions.pgv}")

    // Kotest
    testImplementation("io.kotest:kotest-assertions-core:${Versions.kotest}")

    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Jackson
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
}

application {
    mainClass.set("com.tailrocks.example.api.ExampleApiApplication")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
        javaParameters = true
    }
}

tasks {
    "run" { enabled = false }
    "runShadow" { enabled = false }
    "dockerBuild" { enabled = false }
    "dockerBuildNative" { enabled = false }
    "nativeImage" { enabled = false }
}
