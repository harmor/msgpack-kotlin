package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.NULL

public interface NullPacker {
    public fun pack(value: Nothing?)
}

internal class DefaultNullPacker(private val output: Sink) : NullPacker {
    override fun pack(value: Nothing?) {
        output.write(NULL)
    }
}