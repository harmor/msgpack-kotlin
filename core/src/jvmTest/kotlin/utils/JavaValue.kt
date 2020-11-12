package utils

import io.harmor.msgpack.ArrayValue
import io.harmor.msgpack.BinaryValue
import io.harmor.msgpack.BooleanValue
import io.harmor.msgpack.ExtensionValue
import io.harmor.msgpack.FloatValue
import io.harmor.msgpack.IntegerValue
import io.harmor.msgpack.MapValue
import io.harmor.msgpack.NullValue
import io.harmor.msgpack.StringValue
import io.harmor.msgpack.UnusedValue
import io.harmor.msgpack.Value
import io.harmor.msgpack.isNegative
import io.harmor.msgpack.msgpack
import io.harmor.msgpack.toBigInteger
import io.harmor.msgpack.toLong
import io.harmor.msgpack.toULongOrNull
import org.msgpack.value.NilValue
import org.msgpack.value.ValueFactory

fun org.msgpack.value.IntegerValue.unbox(): Number = when {
    isInByteRange -> toByte()
    isInShortRange -> toShort()
    isInIntRange -> toInt()
    isInLongRange -> toLong()
    else -> toBigInteger()
}

fun org.msgpack.value.NumberValue.unbox(): Number = when (this) {
    is org.msgpack.value.FloatValue -> toDouble()
    is org.msgpack.value.IntegerValue -> unbox()
    else -> throw IllegalArgumentException("unsupported: $this")
}

fun org.msgpack.value.Value.pack(): ByteArray =
    org.msgpack.core.MessagePack.newDefaultBufferPacker().use { packer ->
        packer.packValue(this)
        packer
    }.toByteArray()

fun ByteArray.unpack(): org.msgpack.value.Value =
    org.msgpack.core.MessagePack.newDefaultUnpacker(this).unpackValue()

fun Value.convert(): org.msgpack.value.Value = when (this) {
    is BooleanValue -> ValueFactory.newBoolean(value)
    is StringValue -> ValueFactory.newString(bytes)
    is BinaryValue -> ValueFactory.newBinary(bytes)
    is ArrayValue -> ValueFactory.newArray(map { it.convert() }.toList())
    is NullValue -> ValueFactory.newNil()
    is FloatValue -> if (isDouble) ValueFactory.newFloat(toDouble()) else ValueFactory.newFloat(toFloat())
    is IntegerValue -> convert()
    is MapValue -> ValueFactory.newMapBuilder().apply {
        entries.forEach {
            put(it.key.convert(), it.value.convert())
        }
    }.build()
    is ExtensionValue -> ValueFactory.newExtension(type, bytes)
    is UnusedValue -> TODO()
    else -> throw IllegalArgumentException("$this")
}

fun IntegerValue.convert(): org.msgpack.value.IntegerValue = if (isNegative)
    ValueFactory.newInteger(toLong())
else
    ValueFactory.newInteger(toBigInteger())

fun org.msgpack.value.Value.convert(): Value = when (this) {
    is NilValue -> NullValue
    is org.msgpack.value.BooleanValue -> BooleanValue(boolean)
    is org.msgpack.value.ArrayValue -> ArrayValue(map { it.convert() }.toList())
    is org.msgpack.value.BinaryValue -> BinaryValue(asByteArray())
    is org.msgpack.value.StringValue -> StringValue(asString())
    is org.msgpack.value.FloatValue -> FloatValue(toDouble()) // FIXME: can't discriminate between float and double
    is org.msgpack.value.IntegerValue -> if (isInLongRange)
        IntegerValue.from(toLong())
    else
        IntegerValue.from(toBigInteger().toULongOrNull() ?: throw IllegalArgumentException("$this"))

    is org.msgpack.value.ExtensionValue -> ExtensionValue(type, data)
    is org.msgpack.value.MapValue -> msgpack {
        entrySet().forEach { (key, value) ->
            key.convert() with value.convert()
        }
    }
    else -> throw IllegalArgumentException("$this")
}


