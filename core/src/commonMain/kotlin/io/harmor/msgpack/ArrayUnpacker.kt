package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.ARRAY16
import io.harmor.msgpack.internal.MessageType.ARRAY32
import io.harmor.msgpack.internal.MessageType.FIXARRAY
import io.harmor.msgpack.internal.repeat
import kotlin.experimental.and
import kotlin.ranges.contains

public interface ArrayUnpacker {
    public fun nextArraySize(): Long
}

internal class DefaultArrayUnpacker(private val source: Source) : ArrayUnpacker {

    override fun nextArraySize(): Long = when (val type = source.read()) {
        in FIXARRAY -> (type and 0x0f).toUByte().toLong()
        ARRAY16 -> source.readUShort().toLong()
        ARRAY32 -> source.readUInt().toLong()
        else -> throw MessageTypeException("array", type)
    }
}

public fun MessageUnpacker.nextArray(): ArrayValue = msgpack.array {
    repeat(nextArraySize()) {
        +nextElement()
    }
}