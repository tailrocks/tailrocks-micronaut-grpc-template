object Versions {

    // Languages

    const val kotlin = "1.5.20"

    // Gradle plugins

    // https://plugins.gradle.org/plugin/com.github.ben-manes.versions
    const val gradleVersionsPlugin = "0.39.0"

    // https://plugins.gradle.org/plugin/com.diffplug.spotless
    const val gradleSpotlessPlugin = "5.14.0"

    // https://plugins.gradle.org/plugin/com.gorylenko.gradle-git-properties
    const val gradleGitPropertiesPlugin = "2.3.1"

    // https://plugins.gradle.org/plugin/com.google.protobuf
    const val gradleProtobufPlugin = "0.8.16"

    // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
    const val gradleShadowPlugin = "7.0.0"

    // https://plugins.gradle.org/plugin/io.micronaut.application
    const val gradleMicronautPlugin = "2.0.1"

    // Libraries

    // REMEMBER update micronautVersion in gradle.properties as well
    const val micronaut = "2.5.7"

    // UPDATE together with Micronaut
    // https://repo1.maven.org/maven2/io/micronaut/micronaut-bom/2.5.7/micronaut-bom-2.5.7.pom
    // https://repo1.maven.org/maven2/io/micronaut/sql/micronaut-jooq/3.4.0/micronaut-jooq-3.4.0.pom
    // https://repo1.maven.org/maven2/io/grpc/grpc-bom/1.33.1/grpc-bom-1.33.1.pom
    // https://repo1.maven.org/maven2/io/grpc/grpc-protobuf/1.33.1/grpc-protobuf-1.33.1.pom
    const val postgresql = "42.2.18"
    const val grpc = "1.38.0"
    const val flyway = "7.7.3"
    const val jooq = "3.14.4"
    const val protobuf = "3.17.2"
    const val spotbugs = "4.0.3"
    // end

    const val mapstruct = "1.4.2.Final"
    const val pgv = "0.4.1"
    const val kotest = "4.6.0"

    const val jambalayaChecks = "0.4.0"
    const val jambalayaJunitOpentelemetry = "0.1.0"
    const val jambalayaMapstructProcessor = "0.3.1"
    const val jambalayaMicronautMapstructProtobuf = "0.3.0"
    const val jambalayaOpentelemetry = "0.2.0"
    const val jambalayaProtobuf = "0.4.0"
    const val jambalayaTenancy = "0.4.1"
    const val jambalayaTenancyFlyway = "0.1.1"
    const val jambalayaTenancyGrpcInterface = "0.1.0"
    const val jambalayaTenancyJooq = "0.1.1"

    const val tailrocksExampleJooq = "1.0.0"

    // Project

    const val tailrocksExample = "1.0.0"

}
