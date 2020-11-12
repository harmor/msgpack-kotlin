import java.util.*

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://kotlin.bintray.com/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlin-dev")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

val props = Properties().apply {
    file("../gradle.properties").inputStream().use { load(it) }
}

fun version(target: String) =
    props.getProperty("${target}.version")

dependencies {
    implementation(kotlin("gradle-plugin", version("kotlin")))
}