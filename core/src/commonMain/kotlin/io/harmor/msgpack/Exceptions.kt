package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.nameOf
import kotlin.jvm.JvmOverloads

public expect open class IOException(message: String? = null, cause: Throwable? = null) : Exception
public expect open class EndOfSourceException(message: String? = null) : IOException

public open class MessagePackException : RuntimeException {
    @JvmOverloads
    public constructor(message: String? = null, cause: Throwable? = null) : super(message, cause)
    public constructor(cause: Throwable) : super(cause)
}

public class MessageTypeException : MessagePackException {
    @JvmOverloads
    public constructor(message: String? = null, cause: Throwable? = null) : super(message, cause)
    public constructor(cause: Throwable) : super(cause)

    internal constructor(expected: Byte, found: Byte) : this(nameOf(expected), found)
    internal constructor(expected: ByteArray, found: Byte) : this(expected.joinToString(prefix = "[", postfix = "]") { nameOf(it) }, found)
    internal constructor(expected: String, found: Byte) : this("expected $expected but got ${nameOf(found)}")
}

public class MessageSizeException @JvmOverloads public constructor(message: String? = null)
    : MessagePackException(message)

public class MessageIntegerOverflowException @JvmOverloads public constructor(message: String? = null)
    : MessagePackException(message)