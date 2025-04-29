plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("com.google.protobuf") version "0.9.5"
}

group = "ru.eaglorn.cs"
version = "0.0.1"

val kotlinVersion = "2.1.20"
val kotlinxCoroutinesVersion = "1.10.2"
val ktorVersion = "3.1.2"
val log4jVersion = "2.24.3"
val protobufVersion = "4.30.2"
val zstdVersion = "1.5.7-2"
val eclipseCollections = "11.1.0"

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxCoroutinesVersion}")
    api("io.ktor:ktor-network:${ktorVersion}")
    api("io.ktor:ktor-network-tls:${ktorVersion}")
    api("com.google.protobuf:protobuf-java:${protobufVersion}")
    api("com.github.luben:zstd-jni:${zstdVersion}")
    api("org.apache.logging.log4j:log4j-api:${log4jVersion}")
    api("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    api("org.apache.logging.log4j:log4j-slf4j2-impl:${log4jVersion}")
    api("org.eclipse.collections:eclipse-collections-api:${eclipseCollections}")
    api("org.eclipse.collections:eclipse-collections:${eclipseCollections}")
}

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
}

kotlin {
    jvmToolchain(23)
}
