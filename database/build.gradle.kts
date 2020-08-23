plugins {
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("Database") {
        packageName = "database"
    }
}

kotlin {

    jvm()
    js {
        browser()
    }

    @Suppress("UNUSED_VARIABLE") // source sets
    sourceSets {

        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated/sqldelight/code")

            dependencies {
                implementation(

                    // Modules
                    entities(),
                    domain(),

                    // Kotlin
                    kotlin("stdlib-common"),

                    // Koin
                    koin("core")
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

        val jvmMain by getting {
            dependencies {
                implementation(
                    sqlDelightDriver("sqlite")
                )
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies()
                )
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(

                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
