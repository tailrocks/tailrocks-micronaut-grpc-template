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
    // FIXME replace with real module name
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

    // TODO replace with final version
    api("io.github.expatiat.micronaut.opentelemetry:opentelemetry:0.1.0-SNAPSHOT")

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
    api("io.github.expatiat.jambalaya:jambalaya-opentelemetry:${Versions.jambalayaOpentelemetry}")
    api("io.github.expatiat.jambalaya:jambalaya-tenancy:${Versions.jambalayaTenancy}")
    api("io.github.expatiat.jambalaya:jambalaya-tenancy-flyway:${Versions.jambalayaTenancyFlyway}")
    api("io.github.expatiat.jambalaya:jambalaya-tenancy-grpc-interface:${Versions.jambalayaTenancyGrpcInterface}")
    api("io.github.expatiat.jambalaya:jambalaya-tenancy-jooq:${Versions.jambalayaTenancyJooq}")

    // Logback
    api("ch.qos.logback:logback-classic")
}
