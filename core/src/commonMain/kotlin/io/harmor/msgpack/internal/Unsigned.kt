package io.harmor.msgpack.internal

import kotlin.contracts.contract

internal operator fun ULong.compareTo(value: Long) =
    if (value >= 0) compareTo(value.toULong()) else 1

internal operator fun ULong.compareTo(value: Int) =
    if (value >= 0) compareTo(value.toUInt()) else 1

internal operator fun UInt.compareTo(value: Int) =
    toLong().compareTo(value)

internal operator fun UByte.compareTo(value: Byte) =
    toInt().compareTo(value)

internal operator fun Int.compareTo(value: UByte) =
    compareTo(value.toInt())

internal operator fun Int.compareTo(value: UShort) =
    compareTo(value.toInt())

internal operator fun Long.compareTo(value: UByte) =
    compareTo(value.toInt())

internal operator fun Long.compareTo(value: UShort) =
    compareTo(value.toInt())

internal operator fun Long.compareTo(value: UInt) =
    compareTo(value.toLong())

internal operator fun LongRange.contains(value: ULong) =
    value in start.toULong()..endInclusive.toULong()

internal operator fun IntRange.contains(value: ULong) =
    value in start.toULong()..endInclusive.toULong()

internal operator fun ClosedRange<UInt>.contains(value: Long) =
    value in start.toLong()..endInclusive.toLong()

internal operator fun ClosedRange<UInt>.contains(value: Int) =
    value in start.toLong()..endInclusive.toLong()

internal operator fun ClosedRange<UInt>.contains(value: Short) =
    value in start.toLong()..endInclusive.toLong()

internal inline fun repeat(times: Long, action: (Long) -> Unit) {
    contract { callsInPlace(action) }

    for (index in 0L until times) {
        action(index)
    }
}

internal fun Byte.toULongOrNull() =
    if (this >= 0) toULong() else null

internal fun Short.toULongOrNull() =
    if (this >= 0) toULong() else null

internal fun Int.toULongOrNull() =
    if (this >= 0) toULong() else null

internal fun Byte.toUIntOrNull() =
    if (this >= 0) toUInt() else null

internal fun Short.toUIntOrNull() =
    if (this >= 0) toUInt() else null

internal fun Long.toUIntOrNull() =
    if (this >= 0) toUInt() else null

internal fun Byte.toUShortOrNull() =
    if (this >= 0) toUShort() else null

internal fun Short.toUShortOrNull() =
    if (this >= 0) toUShort() else null

internal fun Long.toUShortOrNull() =
    if (this in UShort.RANGE) toUShort() else null

internal fun UByte.toByteOrNull() =
    if (this <= Byte.MAX_VALUE) toByte() else null

internal fun Byte.toUByteOrNull() =
    if (this >= 0) toUByte() else null

internal fun Short.toUByteOrNull() =
    if (this in UByte.RANGE) toUByte() else null

internal fun Int.toUByteOrNull() =
    if (this in UByte.RANGE) toUByte() else null

internal fun Long.toUByteOrNull() =
    if (this in UByte.RANGE) toUByte() else null

internal fun Short.toByteOrNull() =
    if (this in Byte.RANGE) toByte() else null

internal fun Long.toByteOrNull() =
    if (this in Byte.RANGE) toByte() else null

internal fun Int.toShortOrNull() =
    if (this in Short.RANGE) toShort() else null

internal fun Long.toShortOrNull() =
    if (this in Short.RANGE) toShort() else null

internal fun UShort.toByteOrNull() =
    if (this <= Byte.MAX_VALUE.toUShort()) toByte() else null

internal fun UShort.toShortOrNull() =
    if (this <= Short.MAX_VALUE.toUShort()) toShort() else null

internal fun UShort.toUByteOrNull() =
    if (this <= UByte.MAX_VALUE) toUByte() else null

internal fun UInt.toByteOrNull() =
    if (this <= Byte.MAX_VALUE.toUShort()) toByte() else null

internal fun UInt.toShortOrNull() =
    if (this <= Short.MAX_VALUE.toUShort()) toShort() else null

internal fun UInt.toUByteOrNull() =
    if (this <= UByte.MAX_VALUE) toUByte() else null

internal fun UInt.toUShortOrNull() =
    if (this <= UShort.MAX_VALUE) toUShort() else null

internal fun ULong.toByteOrNull() =
    if (this <= Byte.MAX_VALUE.toUInt()) toByte() else null

internal fun ULong.toShortOrNull() =
    if (this <= Short.MAX_VALUE.toUInt()) toShort() else null

internal fun ULong.toIntOrNull() =
    if (this <= Int.MAX_VALUE.toUInt()) toInt() else null

internal fun ULong.toUByteOrNull() =
    if (this <= UByte.MAX_VALUE) toUByte() else null

internal fun ULong.toUShortOrNull() =
    if (this <= UShort.MAX_VALUE) toUShort() else null

internal fun ULong.toUIntOrNull() =
    if (this <= UInt.MAX_VALUE) toUInt() else null

internal fun Long.toULongOrNull() =
    if (this >= 0) toULong() else null

internal fun ULong.toLongOrNull() =
    if (this <= Long.MAX_VALUE) toLong() else null

internal fun Long.toIntOrNull() =
    if (this in Int.RANGE) toInt() else null

internal fun UInt.toIntOrNull() =
    if (this <= Int.MAX_VALUE) toInt() else null

internal fun Int.toByteOrNull() =
    if (this in Byte.RANGE) toByte() else null

internal fun Int.toUIntOrNull() =
    if (this >= 0) toUInt() else null

internal fun Int.toUShortOrNull() =
    if (this in UShort.RANGE) toUShort() else null