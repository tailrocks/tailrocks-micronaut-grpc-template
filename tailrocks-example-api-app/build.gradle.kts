plugins {
    id("com.github.johnrengelman.shadow") version Versions.gradleShadowPlugin
    id("io.micronaut.application") version Versions.gradleMicronautPlugin
}

micronaut {
    version(Versions.micronaut)
    runtime("netty")
    enableNativeImage(true)
    processing {
        incremental(true)
        // FIXME replace package name
        annotations("com.tailrocks.example.api.*")
    }
}

dependencies {
    // subprojects
    implementation(project(":tailrocks-example-api"))
}

application {
    mainClass.set("com.tailrocks.example.api.ExampleApiApplication")
}
