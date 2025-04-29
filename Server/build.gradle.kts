plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("java")
    id("io.ktor.plugin") version "3.0.3"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

application {
    mainClass.set("ru.eaglorn.cs.ServerKt")
    applicationName = "CustomStoryServer"
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val springVersion = "3.4.4"

dependencies {
    implementation(project(":Shared"))
    implementation("org.springframework.boot:spring-boot-starter:$springVersion") {
        exclude("org.springframework.boot","spring-boot-starter-logging")
    }
}

kotlin {
    jvmToolchain(23)
}