plugins {
    id("com.google.devtools.ksp") version "1.6.0-1.0.1" // used for plugin-processor
    kotlin("jvm") version "1.6.0"
    id("dev.schlaubi.mikbot.gradle-plugin") version "1.0.3"
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://schlaubi.jfrog.io/artifactory/mikbot/")
    maven("https://schlaubi.jfrog.io/artifactory/envconf/")
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    // this one is included in the bot itself, therefore we make it compileOnly
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("dev.schlaubi", "mikbot-api", "2.0.1")
    plugin("dev.schlaubi", "mikbot-ktor", "1.0.1")
    ksp("dev.schlaubi", "mikbot-plugin-processor", "1.0.0")
}

mikbotPlugin {
    description.set("This is a cool plugin!")
}

tasks {
    task<Copy>("buildAndCopy") {
        dependsOn(assemblePlugin)
        from(assemblePlugin)
        include("*.zip")
        into("plugins")
    }
}