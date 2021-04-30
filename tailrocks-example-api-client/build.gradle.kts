plugins {
    id("io.micronaut.library") version Versions.gradleMicronautPlugin
}

micronaut {
    version(Versions.micronaut)
    processing {
        incremental(true)
        annotations("com.tailrocks.example.api.*")
    }
}

dependencies {
    // subprojects
    api(project(":tailrocks-example-grpc-interface"))

    // Logback
    api("ch.qos.logback:logback-classic")
}
