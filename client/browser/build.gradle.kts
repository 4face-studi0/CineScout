plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
                    client(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),

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

        val jsMain by getting {
            repositories {
                maven("https://kotlin.bintray.com/kotlin-js-wrappers")
            }
            dependencies {
                implementation(
                    // React
                    Js.kotlinReact(),
                    Js.kotlinReact("dom"),
                    npm(Js.react(), REACT_VERSION),
                    npm(Js.react("dom"), REACT_VERSION),

                    // Kotlin styled
                    Js.kotlinStyled(),
                    npm(Js.styledComponents(), STYLED_COMPONENTS_VERSION),
                    npm(Js.inlineStylePrefixer(), INLINE_STYLE_PREFIXER_VERSION)
                )
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies(),
                    mockk()
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
