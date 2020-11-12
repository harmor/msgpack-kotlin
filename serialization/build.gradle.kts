plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    val kotestVersion = version("kotest")

    explicitApi()

    targets {
        jvm()

        js {
            useCommonJs()
            browser()
            nodejs()
        }

        apply(from = rootProject.file("gradle/compile-native-multiplatform.gradle"))
    }

    sourceSets {

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            languageSettings.progressiveMode = true
        }

        val commonMain by getting {
            dependencies {
                implementation("io.harmor:msgpack-core:0.1.0")
                api(kotlinx("kotlinx-serialization", "core"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
            }
        }

        val jvmTest by getting {
            dependencies {
                dependsOn(commonTest)
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
                implementation("io.kotest:kotest-property:$kotestVersion")
                implementation("org.msgpack:msgpack-core:0.8.21")
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

apply(from = rootProject.file("gradle/publish.gradle.kts"))