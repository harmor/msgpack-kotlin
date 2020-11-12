package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.BIN16
import io.harmor.msgpack.internal.MessageType.BIN32
import io.harmor.msgpack.internal.MessageType.BIN8
import io.harmor.msgpack.internal.get

public interface BinaryPacker {
    public fun beginBinary(size: UInt)
    public fun appendBinary(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size)
    public fun appendBinary(byte: Byte)
}

public fun BinaryPacker.pack(value: ByteArray) {
    beginBinary(value.size)
    appendBinary(value)
}

public fun BinaryPacker.beginBinary(size: Int) {
    require(size >= 0)
    beginBinary(size.toUInt())
}

public operator fun BinaryPacker.plusAssign(value: ByteArray): Unit = pack(value)

internal class DefaultBinaryPacker(private val output: Sink) : BinaryPacker {

    override fun beginBinary(size: UInt): Unit = when {
        size <= UByte.MAX_VALUE -> {
            output.write(BIN8, size.toByte())
        }
        size <= UShort.MAX_VALUE -> {
            output.write(BIN16, size[1], size[0])
        }
        else -> {
            output.write(BIN32, size[3], size[2], size[1], size[0])
        }
    }

    override fun appendBinary(bytes: ByteArray, offset: Int, size: Int) {
        output.write(bytes, offset, size)
    }

    override fun appendBinary(byte: Byte) {
        output.write(byte)
    }
}