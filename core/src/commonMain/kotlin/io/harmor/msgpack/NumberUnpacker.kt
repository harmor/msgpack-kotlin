package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.FLOAT32
import io.harmor.msgpack.internal.MessageType.FLOAT64
import io.harmor.msgpack.internal.MessageType.INT16
import io.harmor.msgpack.internal.MessageType.INT32
import io.harmor.msgpack.internal.MessageType.INT64
import io.harmor.msgpack.internal.MessageType.INT8
import io.harmor.msgpack.internal.MessageType.NEGATIVE_FIXINT
import io.harmor.msgpack.internal.MessageType.POSITIVE_FIXINT
import io.harmor.msgpack.internal.MessageType.UINT16
import io.harmor.msgpack.internal.MessageType.UINT32
import io.harmor.msgpack.internal.MessageType.UINT64
import io.harmor.msgpack.internal.MessageType.UINT8
import io.harmor.msgpack.internal.asBigEndianInt
import io.harmor.msgpack.internal.asBigEndianLong
import io.harmor.msgpack.internal.asBigEndianShort
import io.harmor.msgpack.internal.toLongOrNull
import io.harmor.msgpack.internal.toULongOrNull
import kotlin.ranges.contains

public interface NumberUnpacker {

    public fun nextByte(): Byte
    public fun nextShort(): Short
    public fun nextInt(): Int
    public fun nextLong(): Long
    public fun nextUByte(): UByte
    public fun nextUShort(): UShort
    public fun nextUInt(): UInt
    public fun nextULong(): ULong
    public fun nextFloat(): Float
    public fun nextDouble(): Double

    public fun skipNumber()
    public fun nextNumber(): NumberValue
}

internal class DefaultNumberUnpacker(private val source: Source) : NumberUnpacker {

    override fun nextByte() = when (val number = nextLong()) {
        in Byte.MIN_VALUE..Byte.MAX_VALUE -> number.toByte()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextShort() = when (val number = nextLong()) {
        in Short.MIN_VALUE..Short.MAX_VALUE -> number.toShort()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextInt() = when (val number = nextLong()) {
        in Int.MIN_VALUE..Int.MAX_VALUE -> number.toInt()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextLong(): Long = when (val type = source.read()) {
        in NEGATIVE_FIXINT -> type.toLong()
        in POSITIVE_FIXINT -> type.toUByte().toLong()
        INT8 -> source.read().toLong()
        INT16 -> source.readShort().toLong()
        INT32 -> source.readInt().toLong()
        INT64 -> source.readLong()
        UINT8 -> source.read().toUByte().toLong()
        UINT16 -> source.readUShort().toLong()
        UINT32 -> source.readUInt().toLong()
        UINT64 -> source.readULong().toLongOrNull() ?: throw MessageIntegerOverflowException()
        else -> throw MessageTypeException("integer", type)
    }

    override fun nextUByte() = when (val number = nextULong()) {
        in UByte.MIN_VALUE..UByte.MAX_VALUE -> number.toUByte()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextUShort() = when (val number = nextULong()) {
        in UShort.MIN_VALUE..UShort.MAX_VALUE -> number.toUShort()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextUInt() = when (val number = nextULong()) {
        in UInt.MIN_VALUE..UInt.MAX_VALUE -> number.toUInt()
        else -> throw MessageIntegerOverflowException()
    }

    override fun nextULong() = when (source.read()) {
        UINT64 -> source.readULong()
        else -> nextLong().toULongOrNull() ?: throw MessageIntegerOverflowException()
    }

    override fun nextFloat() =
        when (val type = source.read()) {
            FLOAT32 -> Float.fromBits(source.readInt())
            else -> throw MessageTypeException(FLOAT32, type)
        }

    override fun nextDouble() =
        when (val type = source.read()) {
            FLOAT32 -> Float.fromBits(source.readInt()).toDouble()
            FLOAT64 -> Double.fromBits(source.readLong())
            else -> throw MessageTypeException(byteArrayOf(FLOAT64, FLOAT32), type)
        }

    override fun nextNumber(): NumberValue = when (source.peek()) {
        FLOAT32 -> FloatValue(nextFloat())
        FLOAT64 -> FloatValue(nextDouble())
        UINT64 -> IntegerValue.from(nextULong())
        else -> IntegerValue.from(nextLong())
    }

    override fun skipNumber() {
        when (val type = source.read()) {
            in POSITIVE_FIXINT -> return
            in NEGATIVE_FIXINT -> return
            UINT8, INT8 -> 1L
            UINT16, INT16 -> 2L
            UINT32, INT32, FLOAT32 -> 4L
            UINT64, INT64, FLOAT64 -> 8L
            else -> throw MessageTypeException("number", type)
        }.let {
            source.skip(it)
        }
    }
}

internal fun Source.readShort() =
    read(2).asBigEndianShort()

internal fun Source.readInt() =
    read(4).asBigEndianInt()

internal fun Source.readLong() =
    read(8).asBigEndianLong()

internal fun Source.readUShort() =
    readShort().toUShort()

internal fun Source.readUInt() =
    readInt().toUInt()

internal fun Source.readULong() =
    readLong().toULong()




