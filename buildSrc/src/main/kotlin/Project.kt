import org.gradle.api.Project

fun Project.version(target: String): String =
    prop("${target}.version")

internal val Project.kotlinVersion: String
    get() = version("kotlin")