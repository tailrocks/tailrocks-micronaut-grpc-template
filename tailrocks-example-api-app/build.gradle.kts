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
    // FIXME replace with real module name
    implementation(project(":tailrocks-example-api"))
}

application {
    // FIXME replace with real class name
    mainClass.set("com.tailrocks.example.api.ExampleApiApplication")
}
