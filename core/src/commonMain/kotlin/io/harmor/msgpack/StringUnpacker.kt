package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.FIXSTRING
import io.harmor.msgpack.internal.MessageType.STRING16
import io.harmor.msgpack.internal.MessageType.STRING32
import io.harmor.msgpack.internal.MessageType.STRING8
import kotlin.experimental.and
import kotlin.ranges.contains

public interface StringUnpacker {
    public fun nextStringSize(): Long
}

internal class DefaultStringUnpacker(private val source: Source) : StringUnpacker {

    override fun nextStringSize(): Long = when (val type = source.read()) {
        in FIXSTRING -> (type and 0x1F).toLong()
        STRING8 -> source.read().toUByte().toLong()
        STRING16 -> source.readUShort().toLong()
        STRING32 -> source.readUInt().toLong()
        else -> throw MessageTypeException("string", type)
    }
}

public fun MessageUnpacker.skipString(): Unit =
    skip(nextStringSize())

public fun MessageUnpacker.nextString(): StringValue =
    nextStringSize().let {
        if (it == 0L)
            msgpack.str("")
        else
            msgpack.str(read(it))
    }
