package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.FALSE
import io.harmor.msgpack.internal.MessageType.TRUE

public interface BooleanUnpacker {
    public fun nextBoolean(): BooleanValue
}

internal class DefaultBooleanUnpacker(private val source: Source) : BooleanUnpacker {
    override fun nextBoolean() = when (val type = source.read()) {
        TRUE -> BooleanValue.TRUE
        FALSE -> BooleanValue.FALSE
        else -> throw MessageTypeException("boolean", type)
    }
}

public fun BooleanUnpacker.skipBoolean() {
    nextBoolean()
}