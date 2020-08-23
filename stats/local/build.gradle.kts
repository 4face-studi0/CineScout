plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm()
    js {
        browser()
    }

    @Suppress("UNUSED_VARIABLE") // source sets
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(

                    // Modules
                    entities(),
                    domain(),
                    database(),
                    stats(),

                    // Kotlin
                    kotlin("stdlib-common"),

                    // Other
                    klock(),
                    koin("core")

                    // SqlDelight
                    // sqlDelight()
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(
                    *commonTestDependencies()
                )
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies(),
                    mockk(),
                    sqlDelightDriver("sqlite")
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
