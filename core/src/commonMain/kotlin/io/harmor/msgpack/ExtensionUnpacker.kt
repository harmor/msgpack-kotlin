package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.EXT16
import io.harmor.msgpack.internal.MessageType.EXT32
import io.harmor.msgpack.internal.MessageType.EXT8
import io.harmor.msgpack.internal.MessageType.FIXEXT1
import io.harmor.msgpack.internal.MessageType.FIXEXT16
import io.harmor.msgpack.internal.MessageType.FIXEXT2
import io.harmor.msgpack.internal.MessageType.FIXEXT4
import io.harmor.msgpack.internal.MessageType.FIXEXT8

public interface ExtensionUnpacker {
    public fun nextExtensionHeader(): Pair<Byte, Long>
}

public fun MessageUnpacker.nextExtension(): ExtensionValue =
    nextExtensionHeader().let { (type, size) ->
        ExtensionValue(type, read(size))
    }

public fun MessageUnpacker.skipExtension(): Unit =
    skip(1L + nextExtensionHeader().second)

internal class DefaultExtensionUnpacker(private val source: Source) : ExtensionUnpacker {

    override fun nextExtensionHeader(): Pair<Byte, Long> {
        val tag = source.read()

        return when (tag) {
            FIXEXT1 -> source.read() to 1L
            FIXEXT2 -> source.read() to 2L
            FIXEXT4 -> source.read() to 4L
            FIXEXT8 -> source.read() to 8L
            FIXEXT16 -> source.read() to 16L
            EXT8 -> {
                val length = source.read().toUByte().toLong()
                source.read() to length
            }
            EXT16 -> {
                val length = source.readUShort().toLong()
                source.read() to length
            }
            EXT32 -> {
                val length = source.readUInt().toLong()
                source.read() to length
            }
            else -> throw MessageTypeException("ext", tag)
        }
    }
}