package io.harmor.msgpack

actual open class IOException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause)
actual open class EndOfSourceException actual constructor(message: String?) : IOException(message)