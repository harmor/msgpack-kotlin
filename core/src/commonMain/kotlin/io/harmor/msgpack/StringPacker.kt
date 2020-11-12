package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.STRING16
import io.harmor.msgpack.internal.MessageType.STRING32
import io.harmor.msgpack.internal.MessageType.STRING8
import io.harmor.msgpack.internal.compareTo
import io.harmor.msgpack.internal.get

public interface StringPacker {
    public fun pack(value: String)
}

public fun StringPacker.pack(value: Char) {
    pack(value.toString())
}

public operator fun StringPacker.plusAssign(value: Char): Unit = pack(value)
public operator fun StringPacker.plusAssign(value: String): Unit = pack(value)

internal class DefaultStringPacker(private val output: Sink) : StringPacker {

    override fun pack(value: String) {
        val data = value.encodeToByteArray()
        val length = data.size
        when {
            length < 32 -> {
                output.write((0b10100000 + length).toByte())
            }
            length <= UByte.MAX_VALUE -> {
                output.write(STRING8)
                output.write(length.toUByte().toByte())
            }
            length <= UShort.MAX_VALUE -> {
                output.write(STRING16)
                output.write(length[1], length[0])
            }
            else -> {
                output.write(STRING32)
                output.write(length[3], length[2], length[1], length[0])
            }
        }
        output.write(data)
    }
}