pluginManagement {

    operator fun Settings.get(name: String): String? =
        this.javaClass.getMethod("getProperty", String::class.java).invoke(this, name) as String?

    val kotlinVersion = settings["kotlin.version"]

    plugins {
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
    }
}

rootProject.name = "msgpack-kotlin"

include("core")
//include("serialization")
