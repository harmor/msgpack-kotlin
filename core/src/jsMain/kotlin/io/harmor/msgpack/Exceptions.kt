package io.harmor.msgpack

public actual open class IOException public actual constructor(message: String?, cause: Throwable?) : Exception(message, cause)
public actual open class EndOfSourceException public actual constructor(message: String?) : IOException(message, null)