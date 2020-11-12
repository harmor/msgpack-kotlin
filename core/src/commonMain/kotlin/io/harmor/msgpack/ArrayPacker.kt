package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.ARRAY16
import io.harmor.msgpack.internal.MessageType.ARRAY32
import io.harmor.msgpack.internal.get

public interface ArrayPacker {
    public fun beginArray(elements: UInt)
}

public fun ArrayPacker.beginArray(elements: Int) {
    require(elements >= 0)
    beginArray(elements.toUInt())
}

internal class DefaultArrayPacker(private val output: Sink) : ArrayPacker {
    override fun beginArray(elements: UInt) {
        when {
            elements <= 15U -> output.write((0b10010000 + elements.toByte()).toByte())
            elements <= UShort.MAX_VALUE -> output.write(ARRAY16, elements[1], elements[0])
            else -> output.write(ARRAY32, elements[3], elements[2], elements[1], elements[0])
        }
    }
}