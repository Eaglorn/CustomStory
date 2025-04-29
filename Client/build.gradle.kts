plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.runtime") version "1.13.1"
}

application {
    mainClass = "ru.eaglorn.cs.ClientKt"
    applicationName = "CustomStoryClient"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Shared"))

}

javafx {
    version = "24.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

runtime {
    options.add("--strip-debug")
    options.add("--compress")
    options.add("2")
    options.add("--no-header-files")
    options.add("--no-man-pages")
    targetPlatform("win") {
        jdkHome = jdkDownload("https://github.com/AdoptOpenJDK/semeru23-binaries/releases/download/jdk-23.0.1%2B11_openj9-0.48.0/ibm-semeru-open-jdk_x64_windows_23.0.1_11_openj9-0.48.0.zip")
    }
    launcher {
        noConsole = true
    }
    jpackage {
        val imgType = "png"
        imageOptions.add("--icon")
        imageOptions.add("src/main/resources/hellofx.$imgType")
        installerOptions.add("--resource-dir")
        installerOptions.add("src/main/resources")
        installerOptions.add("--vendor")
        installerOptions.add("Acme Corporation")
        installerOptions.add("--win-per-user-install")
        installerOptions.add("--win-dir-chooser")
        installerOptions.add("--win-menu")
        installerOptions.add("--win-shortcut")
    }
}

kotlin {
    jvmToolchain(23)
}