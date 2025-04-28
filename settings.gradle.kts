plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "CustomStory"
include("Server")
include("Client")
include("Shared")