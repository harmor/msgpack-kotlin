package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.EXT16
import io.harmor.msgpack.internal.MessageType.EXT32
import io.harmor.msgpack.internal.MessageType.EXT8
import io.harmor.msgpack.internal.MessageType.FIXEXT1
import io.harmor.msgpack.internal.MessageType.FIXEXT16
import io.harmor.msgpack.internal.MessageType.FIXEXT2
import io.harmor.msgpack.internal.MessageType.FIXEXT4
import io.harmor.msgpack.internal.MessageType.FIXEXT8
import io.harmor.msgpack.internal.get

public interface ExtensionPacker {
    public fun beginExtension(type: Byte, size: UInt)
    public fun appendExtension(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size)
    public fun appendExtension(byte: Byte)
}

public fun ExtensionPacker.pack(type: Byte, value: ByteArray) {
    beginExtension(type, value.size)
    appendExtension(value)
}

public fun ExtensionPacker.beginExtension(type: Byte, size: Int) {
    require(size >= 0)
    beginExtension(type, size.toUInt())
}

internal class DefaultExtensionPacker(private val output: Sink) : ExtensionPacker {
    override fun beginExtension(type: Byte, size: UInt): Unit =
        when {
            size == 1U ->
                output.write(FIXEXT1, type)
            size == 2U ->
                output.write(FIXEXT2, type)
            size == 4U ->
                output.write(FIXEXT4, type)
            size == 8U ->
                output.write(FIXEXT8, type)
            size == 16U ->
                output.write(FIXEXT16, type)
            size <= UByte.MAX_VALUE ->
                output.write(EXT8, size.toByte(), type)
            size <= UShort.MAX_VALUE ->
                output.write(EXT16, size[1], size[0], type)
            else ->
                output.write(EXT32, size[3], size[2], size[1], size[0], type)
        }

    override fun appendExtension(bytes: ByteArray, offset: Int, size: Int) {
        output.write(bytes, offset, size)
    }

    override fun appendExtension(byte: Byte) {
        output.write(byte)
    }
}