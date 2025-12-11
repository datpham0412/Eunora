import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {

    androidTarget()

    val xcf = XCFramework()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.5")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.5")
            }
        }

        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}
// Generate API key config
val generateApiKeyConfig by tasks.registering {
    val envFile = rootProject.file(".env")
    val envProps = Properties()
    if(envFile.exists()){
        envProps.load(envFile.inputStream())
    }

    val apiKey = System.getenv("GEMINI_API_KEY") ?: envProps.getProperty("GEMINI_API_KEY") ?: ""
    val outputDir = layout.buildDirectory.dir("generated/kotlin").get().asFile
    val outputFile = File(outputDir, "com/example/shared/ApiConfig.kt")

    outputs.file(outputFile)

    doLast {
        outputFile.parentFile.mkdirs()
        outputFile.writeText("""
package com.example.shared

object ApiConfig {
    const val GEMINI_API_KEY: String = "$apiKey"
}
        """.trimIndent())
    }
}

kotlin {
    sourceSets.commonMain {
        kotlin.srcDir(generateApiKeyConfig.map { it.outputs.files.singleFile.parentFile.parentFile })
    }
}

tasks.matching { it.name.contains("compile", ignoreCase = true) && it.name.contains("Kotlin", ignoreCase = true) }.configureEach {
    dependsOn(generateApiKeyConfig)
}

android {
    namespace = "com.example.shared"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
}
