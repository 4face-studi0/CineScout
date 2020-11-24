plugins {
    val kotlinVersion = "1.4.20" // Nov 19, 2020
    val sqlDelightVersion = "1.4.4" // Oct 08, 2020

    id("cinescout")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.squareup.sqldelight") version sqlDelightVersion apply false
}

buildscript {
    fun isIntelliJ() =
        System.getenv("__CFBundleIdentifier") == "com.jetbrains.intellij"

    val agpVersion =
        if (isIntelliJ()) "4.0.1"
        else "4.2.0-alpha16"
    val koinVersion = "3.0.0-alpha-2"

    repositories.google()
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.koin:koin-gradle-plugin:$koinVersion")
    }
}

subprojects {

    apply(plugin = "koin")

    afterEvaluate {
        extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()?.apply {
            sourceSets {
                all {
                    languageSettings.enableLanguageFeature("InlineClasses")
                    languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ObsoleteCoroutinesApi")
                    languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
                }
            }
        }
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xallow-jvm-ir-dependencies",
                    "-Xskip-prerelease-check",
                    "-Xopt-in=kotlin.time.ExperimentalTime",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )
            }
        }
    }
}
