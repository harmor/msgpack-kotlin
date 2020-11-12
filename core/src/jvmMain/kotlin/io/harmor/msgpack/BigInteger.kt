package io.harmor.msgpack

import io.harmor.msgpack.internal.get
import java.math.BigInteger

public fun IntegerValue.toBigInteger(): BigInteger = if (isNegative)
    BigInteger.valueOf(toLong())
else
    toULong().let {
        BigInteger(byteArrayOf(
                0, it[7], it[6], it[5], it[4],
                it[3], it[2], it[1], it[0]))
    }

public fun BigInteger.toULongOrNull(): ULong? =
    when {
        signum() < 0 -> null
        bitLength() > ULong.SIZE_BITS -> null
        else -> {
            toByteArray().takeLast(8).asReversed().foldIndexed(0UL) { index: Int, acc: ULong, byte: Byte ->
                acc + (byte.toULong() shl (index * 8))
            }
        }
    }

public fun StructureScope.int(value: BigInteger): IntegerValue = when {
    value.signum() >= 0 -> IntegerValue.from(value.toULongOrNull() ?: throw IllegalArgumentException("$value"))
    value.bitLength() < Long.SIZE_BITS -> IntegerValue.from(value.toLong())
    else -> throw IllegalArgumentException("$value")
}