package io.harmor.msgpack

import io.harmor.msgpack.internal.toByteOrNull
import io.harmor.msgpack.internal.toIntOrNull
import io.harmor.msgpack.internal.toLongOrNull
import io.harmor.msgpack.internal.toShortOrNull
import io.harmor.msgpack.internal.toUByteOrNull
import io.harmor.msgpack.internal.toUIntOrNull
import io.harmor.msgpack.internal.toULongOrNull
import io.harmor.msgpack.internal.toUShortOrNull
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.math.sign

public sealed class NumberValue : Value

public class FloatValue private constructor(private val value: Number) : NumberValue() {

    internal constructor(value: Float) : this(value as Number)
    internal constructor(value: Double) : this(value as Number)

    public val isDouble: Boolean = value is Double

    public fun toDouble(): Double = value.toDouble()
    public fun toFloat(): Float = value.toFloat()

    override fun pack(packer: MessagePacker) {
        when (value) {
            is Float -> packer.pack(value)
            is Double -> packer.pack(value)
        }
    }

    override fun equals(other: Any?): Boolean =
        other is FloatValue && value == other.value

    override fun hashCode(): Int =
        value.hashCode()

    override fun toString(): String = value.toString()
}

public class IntegerValue private constructor(private val value: Any) : NumberValue() {

    internal companion object {
        internal fun from(value: Byte) = IntegerValue(value)
        internal fun from(value: Short) = IntegerValue(value)
        internal fun from(value: Int) = IntegerValue(value)
        internal fun from(value: Long) = IntegerValue(value)
        internal fun from(value: UByte) = IntegerValue(value)
        internal fun from(value: UShort) = IntegerValue(value)
        internal fun from(value: UInt) = IntegerValue(value)
        internal fun from(value: ULong) = IntegerValue(value)
    }

    public val sign: Int by lazy(NONE) {
        when (value) {
            is Byte -> value.toInt().sign
            is Short -> value.toInt().sign
            is Int -> value.sign
            is Long -> value.sign
            is UByte -> if (value.toUInt() == 0U) 0 else 1
            is UShort -> if (value.toUInt() == 0U) 0 else 1
            is UInt -> if (value == 0U) 0 else 1
            is ULong -> if (value.toULong() == 0UL) 0 else 1
            else -> throw IllegalStateException()
        }
    }

    public fun toByteOrNull(): Byte? = when (value) {
        is Byte -> value
        is Short -> value.toByteOrNull()
        is Int -> value.toByteOrNull()
        is Long -> value.toByteOrNull()
        is UByte -> value.toByteOrNull()
        is UShort -> value.toByteOrNull()
        is UInt -> value.toByteOrNull()
        is ULong -> value.toByteOrNull()
        else -> throw IllegalStateException()
    }

    public fun toUByteOrNull(): UByte? = when (value) {
        is Byte -> value.toUByteOrNull()
        is Short -> value.toUByteOrNull()
        is Int -> value.toUByteOrNull()
        is Long -> value.toUByteOrNull()
        is UByte -> value
        is UShort -> value.toUByteOrNull()
        is UInt -> value.toUByteOrNull()
        is ULong -> value.toUByteOrNull()
        else -> throw IllegalStateException()
    }

    public fun toShortOrNull(): Short? = when (value) {
        is Byte -> value.toShort()
        is Short -> value
        is Int -> value.toShortOrNull()
        is Long -> value.toShortOrNull()
        is UByte -> value.toShort()
        is UShort -> value.toShortOrNull()
        is UInt -> value.toShortOrNull()
        is ULong -> value.toShortOrNull()
        else -> throw IllegalStateException()
    }

    public fun toUShortOrNull(): UShort? = when (value) {
        is Byte -> value.toUShortOrNull()
        is Short -> value.toUShortOrNull()
        is Int -> value.toUShortOrNull()
        is Long -> value.toUShortOrNull()
        is UByte -> value.toUShort()
        is UShort -> value
        is UInt -> value.toUShortOrNull()
        is ULong -> value.toUShortOrNull()
        else -> throw IllegalStateException()
    }

    public fun toIntOrNull(): Int? = when (value) {
        is Byte -> value.toInt()
        is Short -> value.toInt()
        is Int -> value
        is Long -> value.toIntOrNull()
        is UByte -> value.toInt()
        is UShort -> value.toInt()
        is UInt -> value.toIntOrNull()
        is ULong -> value.toIntOrNull()
        else -> throw IllegalStateException()
    }

    public fun toUIntOrNull(): UInt? = when (value) {
        is Byte -> value.toUIntOrNull()
        is Short -> value.toUIntOrNull()
        is Int -> value.toUIntOrNull()
        is Long -> value.toUIntOrNull()
        is UByte -> value.toUInt()
        is UShort -> value.toUInt()
        is UInt -> value
        is ULong -> value.toUIntOrNull()
        else -> throw IllegalStateException()
    }

    public fun toLongOrNull(): Long? = when (value) {
        is Byte -> value.toLong()
        is Short -> value.toLong()
        is Int -> value.toLong()
        is Long -> value
        is UByte -> value.toLong()
        is UShort -> value.toLong()
        is UInt -> value.toLong()
        is ULong -> value.toLongOrNull()
        else -> throw IllegalStateException()
    }

    public fun toULongOrNull(): ULong? = when (value) {
        is Byte -> value.toULongOrNull()
        is Short -> value.toULongOrNull()
        is Int -> value.toULongOrNull()
        is Long -> value.toULongOrNull()
        is UByte -> value.toULong()
        is UShort -> value.toULong()
        is UInt -> value.toULong()
        is ULong -> value
        else -> throw IllegalStateException()
    }

    override fun pack(packer: MessagePacker) {
        when (value) {
            is Byte -> packer.pack(value)
            is Short -> packer.pack(value)
            is Int -> packer.pack(value)
            is Long -> packer.pack(value)
            is UByte -> packer.pack(value)
            is UShort -> packer.pack(value)
            is UInt -> packer.pack(value)
            is ULong -> packer.pack(value)
            else -> throw IllegalStateException()
        }
    }

    override fun equals(other: Any?): Boolean =
        other is IntegerValue && if (isPositive) {
            other.isPositive && other.toULong() == toULong()
        } else {
            other.isNegative && other.toLong() == toLong()
        }

    override fun hashCode(): Int = if (isPositive)
        toULong().hashCode()
    else
        toLong().hashCode()

    override fun toString(): String = if (isPositive)
        toULong().toString()
    else
        toLong().toString()
}

public fun IntegerValue.toByte(): Byte = toByteOrNull() ?: throw MessageIntegerOverflowException("Byte: $this")
public fun IntegerValue.toShort(): Short = toShortOrNull() ?: throw MessageIntegerOverflowException("Short: $this")
public fun IntegerValue.toInt(): Int = toIntOrNull() ?: throw MessageIntegerOverflowException("Int: $this")
public fun IntegerValue.toLong(): Long = toLongOrNull() ?: throw MessageIntegerOverflowException("Long: $this")

public fun IntegerValue.toUByte(): UByte = toUByteOrNull() ?: throw MessageIntegerOverflowException("UByte: $this")
public fun IntegerValue.toUShort(): UShort = toUShortOrNull() ?: throw MessageIntegerOverflowException("UShort: $this")
public fun IntegerValue.toUInt(): UInt = toUIntOrNull() ?: throw MessageIntegerOverflowException("UInt: $this")
public fun IntegerValue.toULong(): ULong = toULongOrNull() ?: throw MessageIntegerOverflowException("ULong: $this")

public fun FloatValue.valueEquals(value: Float): Boolean = value == toFloat()
public fun FloatValue.valueEquals(value: Double): Boolean = value == toDouble()

public fun IntegerValue.valueEquals(value: Byte): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: Short): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: Int): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: Long): Boolean = IntegerValue.from(value) == this

public fun IntegerValue.valueEquals(value: UByte): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: UShort): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: UInt): Boolean = IntegerValue.from(value) == this
public fun IntegerValue.valueEquals(value: ULong): Boolean = IntegerValue.from(value) == this

public val IntegerValue.isPositive: Boolean
    get() = sign >= 0

public val IntegerValue.isNegative: Boolean
    get() = !isPositive

