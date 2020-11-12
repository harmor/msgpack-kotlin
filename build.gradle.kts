import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    repositories {
        mavenLocal()
        jcenter()
    }

    group = requireNotNull(project.properties["GROUP"])
    version = requireNotNull(project.properties["VERSION_NAME"])

    if (hasProperty("SNAPSHOT") || System.getenv("SNAPSHOT") != null) {
        version = "$version-SNAPSHOT"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}