import org.gradle.api.Project

fun Project.kotlinx(projectName: String, name: String): String =
    "org.jetbrains.kotlinx:$projectName-$name:${version(projectName)}"