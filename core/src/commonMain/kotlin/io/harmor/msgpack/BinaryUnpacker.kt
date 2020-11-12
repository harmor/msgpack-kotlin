package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.BIN16
import io.harmor.msgpack.internal.MessageType.BIN32
import io.harmor.msgpack.internal.MessageType.BIN8

public interface BinaryUnpacker {
    public fun nextBinarySize(): Long
}

public fun MessageUnpacker.nextBinary(): BinaryValue =
    BinaryValue(read(nextBinarySize()))

public fun MessageUnpacker.skipBinary(): Unit =
    skip(nextBinarySize())

internal class DefaultBinaryUnpacker(private val source: Source) : BinaryUnpacker {

    override fun nextBinarySize(): Long = when (val type = source.read()) {
        BIN8 -> source.read().toUByte().toLong()
        BIN16 -> source.readUShort().toLong()
        BIN32 -> source.readUInt().toLong()
        else -> throw MessageTypeException("bin", type)
    }
}