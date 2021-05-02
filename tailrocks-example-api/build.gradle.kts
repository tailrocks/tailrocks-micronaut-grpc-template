plugins {
    id("io.micronaut.library") version Versions.gradleMicronautPlugin
}

micronaut {
    version(Versions.micronaut)
    runtime("netty")
    processing {
        incremental(true)
        // FIXME replace package name
        annotations("com.tailrocks.example.api.*")
    }
}

dependencies {
    // subprojects
    api(project(":tailrocks-example-grpc-interface"))

    // tailrocks
    // FIXME replace with real dependency
    api("com.tailrocks.domain:tailrocks-example-jooq:${Versions.tailrocksExampleJooq}")

    // Micronaut
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    api("io.micronaut:micronaut-runtime")
    api("io.micronaut:micronaut-management")
    api("io.micronaut:micronaut-http-client")
    api("io.micronaut.grpc:micronaut-grpc-server-runtime")
    api("io.micronaut.flyway:micronaut-flyway")
    api("io.micronaut.sql:micronaut-jdbc-hikari")
    api("io.micronaut.sql:micronaut-jooq")
    api("io.micronaut.data:micronaut-data-tx")

    // MapStruct
    annotationProcessor("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
    api("org.mapstruct:mapstruct:${Versions.mapstruct}")

    // PGV
    api("io.envoyproxy.protoc-gen-validate:pgv-java-grpc:${Versions.pgv}")

    // Jambalaya
    annotationProcessor("io.github.expatiat.jambalaya:jambalaya-mapstruct-processor:${Versions.jambalayaMapstructProcessor}")
    api("io.github.expatiat.jambalaya:jambalaya-checks:${Versions.jambalayaChecks}")
    api("io.github.expatiat.jambalaya:jambalaya-protobuf:${Versions.jambalayaProtobuf}")
    api("io.github.expatiat.jambalaya:jambalaya-micronaut-mapstruct-protobuf:${Versions.jambalayaMicronautMapstructProtobuf}")

    // Logback
    api("ch.qos.logback:logback-classic")
}
