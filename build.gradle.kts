import org.gradle.jvm.toolchain.internal.DefaultToolchainSpec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.devtools.ksp") version "1.6.0-1.0.2" // used for plugin-processor
    kotlin("jvm") version "1.6.0"
    // used for json-serialization and deserialization
    kotlin("plugin.serialization") version "1.5.31"
    id("dev.schlaubi.mikbot.gradle-plugin") version "1.3.0"
}

val experimentalAnnotations =
    listOf("kotlin.RequiresOptIn", "kotlin.time.ExperimentalTime", "kotlin.contracts.ExperimentalContracts")

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

    plugin("dev.schlaubi", "mikbot-ktor", "1.0.2")
    ksp("dev.schlaubi", "mikbot-plugin-processor", "1.0.0")

    // implementation("io.ktor", "ktor-serialization", "1.6.2")
}

mikbotPlugin {
    description.set("This is a cool plugin!")
    provider.set("niggelgame")
    license.set("AGPL-3.0")
}

tasks {
    task<Copy>("buildAndCopy") {
        dependsOn(assemblePlugin)
        from(assemblePlugin)
        include("*.zip")
        into("plugins")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + experimentalAnnotations.map { "-Xopt-in=$it" }
        }
    }

    installBot {
        botVersion.set("2.0.1-SNAPSHOT")
    }
}

kotlin {
    jvmToolchain {
        (this as DefaultToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}