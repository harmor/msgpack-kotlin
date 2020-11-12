package io.harmor.msgpack

import io.harmor.msgpack.Value.Type
import io.harmor.msgpack.Value.Type.ARRAY
import io.harmor.msgpack.Value.Type.BINARY
import io.harmor.msgpack.Value.Type.BOOLEAN
import io.harmor.msgpack.Value.Type.EXTENSION
import io.harmor.msgpack.Value.Type.FLOAT
import io.harmor.msgpack.Value.Type.INTEGER
import io.harmor.msgpack.Value.Type.MAP
import io.harmor.msgpack.Value.Type.NULL
import io.harmor.msgpack.Value.Type.STRING
import io.harmor.msgpack.Value.Type.UNUSED
import io.harmor.msgpack.internal.repeat

public interface MessageUnpacker : StringUnpacker, BooleanUnpacker, NumberUnpacker, BinaryUnpacker,
                                   MapUnpacker, ArrayUnpacker, ExtensionUnpacker, NullUnpacker {

    public fun getType(): Type
    public fun hasNext(): Boolean

    public fun read(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size)
    public fun skip(bytes: Long)
}

public fun MessageUnpacker.read(): Byte =
    ByteArray(1).also { read(it) }[0]

public fun MessageUnpacker.read(size: Long): ByteArray =
    if (size > Int.MAX_VALUE)
        throw MessageSizeException("$size")
    else
        read(size.toInt())

public fun MessageUnpacker.read(size: Int): ByteArray =
    ByteArray(size).also { read(it) }

public fun MessageUnpacker.skipElement() {
    when (getType()) {
        FLOAT, INTEGER -> skipNumber()
        STRING -> skipString()
        MAP -> repeat(nextMapSize()) {
            skipElement()
            skipElement()
        }
        ARRAY -> repeat(nextArraySize()) {
            skipElement()
        }
        BINARY -> skipBinary()
        EXTENSION -> skipExtension()
        BOOLEAN -> skipBoolean()
        NULL -> skipNull()
        UNUSED -> skip(1)
    }
}

public fun MessageUnpacker.nextElement(): Value {
    return when (getType()) {
        FLOAT, INTEGER -> nextNumber()
        STRING -> nextString()
        MAP -> nextMap()
        ARRAY -> nextArray()
        BINARY -> nextBinary()
        EXTENSION -> nextExtension()
        BOOLEAN -> nextBoolean()
        NULL -> nextNull()
        UNUSED -> {
            skip(1)
            UnusedValue
        }
    }
}

public operator fun MessageUnpacker.iterator(): Iterator<Value> =
    object : Iterator<Value> {
        override fun hasNext() = this@iterator.hasNext()
        override fun next() = nextElement()
    }

