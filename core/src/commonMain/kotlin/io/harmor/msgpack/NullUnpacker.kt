package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.NULL

public interface NullUnpacker {
    public fun nextNull(): NullValue
}

public fun NullUnpacker.skipNull() {
    nextNull()
}

internal class DefaultNullUnpacker(private val source: Source) : NullUnpacker {

    override fun nextNull() = when (val type = source.read()) {
        NULL -> NullValue
        else -> throw MessageTypeException(NULL, type)
    }
}