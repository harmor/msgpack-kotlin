package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.MAP16
import io.harmor.msgpack.internal.MessageType.MAP32
import io.harmor.msgpack.internal.get

public interface MapPacker {
    public fun beginMap(elements: UInt)
}

public fun MapPacker.beginMap(elements: Int) {
    require(elements >= 0)
    beginMap(elements.toUInt())
}

internal class DefaultMapPacker(private val encoder: Sink) : MapPacker {
    override fun beginMap(elements: UInt) {
        when {
            elements <= 15U -> encoder.write((0b10000000 + elements.toByte()).toByte())
            elements <= UShort.MAX_VALUE -> encoder.write(MAP16, elements[1], elements[0])
            else -> encoder.write(MAP32, elements[3], elements[2], elements[1], elements[0])
        }
    }
}