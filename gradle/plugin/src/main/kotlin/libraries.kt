import studio.forface.easygradle.dsl.version

fun assert4k() = forface("assert4k") version ASSERT4K_VERSION

fun coroutines(moduleName: String) = kotlinx("coroutines-$moduleName") version COROUTINES_VERSION

fun forface(moduleName: String) = "studio.forface:$moduleName"

fun kermit() = "co.touchlab:kermit" version KERMIT_VERSION
fun klock() = "com.soywiz.korlibs.klock:klock" version KLOCK_VERSION
fun koin(moduleName: String? = null) = "org.koin:koin${moduleName.module()}" version KOIN_VERSION
fun kotlin(moduleName: String) = "org.jetbrains.kotlin:kotlin-$moduleName" version KOTLIN_VERSION
fun kotlinx(moduleName: String) = "org.jetbrains.kotlinx:kotlinx-$moduleName"

fun ktor(moduleName: String) = "io.ktor:ktor-$moduleName" version KTOR_VERSION
fun ktorClient(moduleName: String) = ktor("client-$moduleName")

fun mockk(moduleName: String? = null) = "io.mockk:mockk${moduleName.module()}" version MOCKK_VERSION

fun picnic() = "com.jakewharton.picnic:picnic" version PICNIC_VERSION
fun proton(moduleName: String) = "me.proton.core:$moduleName"

fun serialization(moduleName: String? = null) =
    kotlinx("serialization${moduleName.module()}") version SERIALIZATION_VERSION

fun sqlDelightDriver(platform: String) = "com.squareup.sqldelight:$platform-driver:1.4.0" version SQLDELIGHT_VERSION

object Android {
    fun accompanist(moduleName: String) = "dev.chrisbanes.accompanist:accompanist-$moduleName" version ACCOMPANIST_VERSION
    fun activity() = ktx("activity") version ACTIVITY_VERSION
    fun appCompat() = "androidx.appcompat:appcompat" version APP_COMPAT_VERSION

    fun compose(moduleName: String) = "androidx.compose.${moduleName.substringBefore("-")}:$moduleName" version COMPOSE_VERSION

    fun ktx() = ktx("core") version KTX_VERSION
    fun ktx(moduleName: String) = "androidx.$moduleName:$moduleName-ktx"

    fun ui(moduleName: String) = "androidx.ui:ui-$moduleName" version COMPOSE_VERSION
}

// Groups
fun commonTestDependencies() = arrayOf(
    kotlin("test-common"),
    kotlin("test-annotations-common"),
    assert4k(),
    koin("test")
)

fun jvmTestDependencies() = arrayOf(
    kotlin("test-junit"),
    kotlinx("coroutines-test") version COROUTINES_VERSION
)
