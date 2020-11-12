package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.FALSE
import io.harmor.msgpack.internal.MessageType.TRUE

public interface BooleanPacker {
    public fun pack(value: Boolean)
}

internal operator fun BooleanPacker.plusAssign(value: Boolean) = pack(value)

internal class DefaultBooleanPacker(private val output: Sink) : BooleanPacker {
    override fun pack(value: Boolean) {
        output.write(if (value) TRUE else FALSE)
    }
}