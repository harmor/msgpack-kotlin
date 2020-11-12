package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.FIXMAP
import io.harmor.msgpack.internal.MessageType.MAP16
import io.harmor.msgpack.internal.MessageType.MAP32
import io.harmor.msgpack.internal.repeat
import kotlin.experimental.and
import kotlin.ranges.contains

public interface MapUnpacker {
    public fun nextMapSize(): Long
}

internal class DefaultMapUnpacker(private val source: Source) : MapUnpacker {
    override fun nextMapSize() = when (val type = source.read()) {
        in FIXMAP -> (type and 0x0f).toLong()
        MAP16 -> source.readUShort().toLong()
        MAP32 -> source.readUInt().toLong()
        else -> throw MessageTypeException("map", type)
    }
}

public fun MessageUnpacker.nextMap(): MapValue = msgpack {
    repeat(nextMapSize()) {
        val key = nextElement()
        val value = nextElement()
        put(key, value)
    }
}