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
import io.harmor.msgpack.internal.get
import kotlin.ranges.contains

public interface NumberPacker {
    public fun pack(value: Byte)
    public fun pack(value: Short)
    public fun pack(value: Int)
    public fun pack(value: Long)
    public fun pack(value: UByte)
    public fun pack(value: UShort)
    public fun pack(value: UInt)
    public fun pack(value: ULong)
    public fun pack(value: Float)
    public fun pack(value: Double)
}

public operator fun NumberPacker.plusAssign(value: Byte): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: Short): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: Int): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: Long): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: UByte): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: UShort): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: UInt): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: ULong): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: Float): Unit = pack(value)
public operator fun NumberPacker.plusAssign(value: Double): Unit = pack(value)

internal class CanonicalNumberPacker(private val output: Sink) : NumberPacker {

    override fun pack(value: Byte): Unit =
        when (value) {
            in NEGATIVE_FIXINT -> output.write(value)
            in POSITIVE_FIXINT -> output.write(value)
            else -> output.write(INT8, value)
        }

    override fun pack(value: Short): Unit =
        output.write(INT16, value[1], value[0])

    override fun pack(value: Int): Unit =
        output.write(INT32, value[3], value[2], value[1], value[0])

    override fun pack(value: Long): Unit =
        output.write(INT64, value[7], value[6], value[5], value[4], value[3], value[2], value[1], value[0])

    override fun pack(value: UByte) {
        when {
            value < 128U -> output.write(value.toByte())
            else -> output.write(UINT8, value.toByte())
        }
    }

    override fun pack(value: UShort): Unit =
        output.write(UINT16, value[1], value[0])

    override fun pack(value: UInt): Unit =
        output.write(UINT32, value[3], value[2], value[1], value[0])

    override fun pack(value: ULong): Unit =
        output.write(UINT64, value[7], value[6], value[5], value[4], value[3], value[2], value[1], value[0])

    override fun pack(value: Float) {
        val bits = value.toRawBits()
        output.write(FLOAT32, bits[3], bits[2], bits[1], bits[0])
    }

    override fun pack(value: Double) {
        val bits = value.toRawBits()
        output.write(FLOAT64, bits[7], bits[6], bits[5], bits[4], bits[3], bits[2], bits[1], bits[0])
    }
}

internal class DefaultNumberPacker private constructor(private val delegate: CanonicalNumberPacker)
    : NumberPacker by delegate {

    constructor(output: Sink) : this(CanonicalNumberPacker(output))

    override fun pack(value: Short): Unit =
        when (value) {
            in Byte.MIN_VALUE..Byte.MAX_VALUE -> pack(value.toByte())
            else -> delegate.pack(value)
        }

    override fun pack(value: Int): Unit =
        when (value) {
            in Short.MIN_VALUE..Short.MAX_VALUE -> pack(value.toShort())
            else -> delegate.pack(value)
        }

    override fun pack(value: Long): Unit =
        when (value) {
            in Int.MIN_VALUE..Int.MAX_VALUE -> pack(value.toInt())
            else -> delegate.pack(value)
        }

    override fun pack(value: UShort): Unit = when {
        value <= UByte.MAX_VALUE -> pack(value.toUByte())
        else -> delegate.pack(value)
    }

    override fun pack(value: UInt): Unit = when {
        value <= UShort.MAX_VALUE -> pack(value.toUShort())
        else -> delegate.pack(value)
    }

    override fun pack(value: ULong): Unit = when {
        value <= UInt.MAX_VALUE -> pack(value.toUInt())
        else -> delegate.pack(value)
    }
}