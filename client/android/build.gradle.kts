plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(

        // Modules
        entities(),
        domain(),
        client(),
        design(),

        // Kotlin
        kotlin("stdlib-jdk8"),
        coroutines("android"),

        // Log
        kermit(),

        // Android
        Android.activity(),
        Android.appCompat(),
        Android.ktx(),

        // Compose
        Android.accompanist("coil"),
        Android.compose("animation"),
        Android.compose("foundation"),
        Android.compose("foundation-layout"),
        Android.compose("material"),
        Android.compose("material-icons-extended"),
        Android.compose("runtime"),
        Android.compose("ui"),
        Android.ui("tooling"),

        // Koin
        koin("android")
    )

    testImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockk(),

        // Compose
        Android.ui("test")
    )

    androidTestImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockk("android"),

        // Compose
        Android.ui("test")
    )
}

version = studio.forface.easygradle.dsl.Version(0, 1)

// Configuration accessors
fun DependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("implementation", dep)
}

fun DependencyHandler.testImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("testImplementation", dep)
}

fun DependencyHandler.androidTestImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("androidTestImplementation", dep)
}
