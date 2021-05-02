plugins {
    id("io.micronaut.library") version Versions.gradleMicronautPlugin
}

version = Versions.tailrocksExample

micronaut {
    version(Versions.micronaut)
    processing {
        incremental(true)
        // FIXME replace package name
        annotations("com.tailrocks.example.api.*")
    }
}

dependencies {
    // subprojects
    api(project(":tailrocks-example-grpc-interface"))

    // Logback
    api("ch.qos.logback:logback-classic")
}
