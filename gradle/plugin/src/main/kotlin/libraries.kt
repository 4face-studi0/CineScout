import studio.forface.easygradle.dsl.version

fun assert4k() = forface("assert4k") version ASSERT4K_VERSION

fun coroutines(moduleName: String) = kotlinx("coroutines-$moduleName") version COROUTINES_VERSION

fun forface(moduleName: String) = "studio.forface:$moduleName"

fun jetbrains(moduleName: String) = "org.jetbrains:$moduleName"

fun klock() = "com.soywiz.korlibs.klock:klock" version KLOCK_VERSION
fun koin(moduleName: String? = null) = "org.koin:koin${moduleName.module()}" version KOIN_VERSION
fun kotlin(moduleName: String) = "org.jetbrains.kotlin:kotlin-$moduleName" version KOTLIN_VERSION
fun kotlinx(moduleName: String) = "org.jetbrains.kotlinx:kotlinx-$moduleName"

fun ktor(moduleName: String) = "io.ktor:ktor-$moduleName" version KTOR_VERSION
fun ktorClient(moduleName: String) = ktor("client-$moduleName")

fun mockk() = "io.mockk:mockk" version MOCKK_VERSION

fun picnic() = "com.jakewharton.picnic:picnic" version PICNIC_VERSION
fun proton(moduleName: String) = "me.proton.core:$moduleName"

fun serialization(moduleName: String? = null) =
    kotlinx("serialization${moduleName.module()}") version SERIALIZATION_VERSION

fun sqlDelightDriver(platform: String) = "com.squareup.sqldelight:$platform-driver:1.4.0" version SQLDELIGHT_VERSION

object Js {
    fun inlineStylePrefixer() = "inline-style-prefixer"
    fun kotlinReact(moduleName: String? = null) = jetbrains("kotlin-react${moduleName.module()}") version KOTLIN_REACT_VERSION
    fun kotlinStyled(moduleName: String? = null) = jetbrains("kotlin-styled${moduleName.module()}") version KOTLIN_STYLED_VERSION
    fun react(moduleName: String? = null) = "react${moduleName.module()}"
    fun styledComponents() = "styled-components"
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
