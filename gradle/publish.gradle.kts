/*
 * Adapted from:
 * https://github.com/reduxkotlin/redux-kotlin/blob/master/gradle/publish.gradle
 * https://github.com/vanniktech/gradle-maven-publish-plugin
 */
apply(plugin = "maven-publish")

fun isReleaseBuild() =
    !version.toString().contains("SNAPSHOT")

fun getReleaseRepositoryUrl() =
    properties["RELEASE_REPOSITORY_URL"]?.toString() ?: "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

fun getSnapshotRepositoryUrl() =
    properties["SNAPSHOT_REPOSITORY_URL"]?.toString() ?: "https://oss.sonatype.org/content/repositories/snapshots/"

fun getRepositoryUrl() = when (isReleaseBuild()) {
    true -> getReleaseRepositoryUrl()
    else -> getSnapshotRepositoryUrl()
}

fun getRepositoryUsername() =
    findProperty("SONATYPE_NEXUS_USERNAME")?.toString() ?: System.getenv("SONATYPE_NEXUS_USERNAME") ?: ""

fun getRepositoryPassword() =
    findProperty("SONATYPE_NEXUS_PASSWORD")?.toString() ?: System.getenv("SONATYPE_NEXUS_PASSWORD") ?: ""

val emptySourcesJar = tasks.create("emptySourcesJar", Jar::class) {
    classifier = "sources"
}

val POM_NAME: String by project

val POM_ARTIFACT_ID: String? by project

val POM_DESCRIPTION: String by project
val POM_URL: String by project

val POM_LICENCE_NAME: String by project
val POM_LICENCE_URL: String by project
val POM_LICENCE_DIST: String by project

val POM_SCM_URL: String by project
val POM_SCM_CONNECTION: String by project
val POM_SCM_DEV_CONNECTION: String by project

val POM_DEVELOPER_ID: String by project
val POM_DEVELOPER_NAME: String by project
val POM_DEVELOPER_URL: String by project

// https://docs.gradle.org/5.0/userguide/kotlin_dsl.html#project_extensions_and_conventions
configure<PublishingExtension> {

    publications.withType(MavenPublication::class).all {

        POM_ARTIFACT_ID?.let {
            artifactId = artifactId.replace(Regex("^${project.name}"), it)
        }

        pom.withXml {
            asNode().apply {
                appendNode("name", POM_NAME)
                appendNode("description", POM_DESCRIPTION)
                appendNode("url", POM_URL)
            }
        }

        with(pom) {
            licenses {
                license {
                    name.set(POM_LICENCE_NAME)
                    url.set(POM_LICENCE_URL)
                    distribution.set(POM_LICENCE_DIST)
                }
            }
            scm {
                url.set(POM_SCM_URL)
                connection.set(POM_SCM_CONNECTION)
                developerConnection.set(POM_SCM_DEV_CONNECTION)
            }
            developers {
                developer {
                    id.set(POM_DEVELOPER_ID)
                    name.set(POM_DEVELOPER_NAME)
                    url.set(POM_DEVELOPER_URL)
                }
            }
        }
    }

    afterEvaluate {
        publications.getByName("kotlinMultiplatform", MavenPublication::class) {
            // Source jars are only created for platforms, not the common artifact.
            artifact(emptySourcesJar)
        }
    }

    repositories {
        maven {
            setUrl(getRepositoryUrl())

            credentials {
                username = getRepositoryUsername()
                password = getRepositoryPassword()
            }
        }
        /*
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/renaudcerrato/msgpack-kotlin")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        */
    }
}