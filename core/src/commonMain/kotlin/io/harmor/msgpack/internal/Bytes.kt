package io.harmor.msgpack.internal

internal operator fun Short.get(position: Int) =
    toInt()[position]

internal operator fun Int.get(position: Int) =
    ((this shr (position * 8)) and 0xff).toByte()

internal operator fun Long.get(position: Int) =
    ((this shr (position * 8)) and 0xff).toByte()

internal operator fun UShort.get(position: Int) =
    toUInt()[position]

internal operator fun UInt.get(position: Int) =
    ((this shr (position * 8)) and 0xffu).toByte()

internal operator fun ULong.get(position: Int) =
    ((this shr (position * 8)) and 0xffu).toByte()

internal fun ByteArray.asBigEndianShort(): Short =
    (((this[0].toInt() and 0xff) shl 8) or
            (this[1].toInt() and 0xff)).toShort()

internal fun ByteArray.asBigEndianUShort() =
    asBigEndianShort().toUShort()

internal fun ByteArray.asBigEndianInt() =
    ((this[0].toInt() and 0xff) shl 24) or
            ((this[1].toInt() and 0xff) shl 16) or
            ((this[2].toInt() and 0xff) shl 8) or
            (this[3].toInt() and 0xff)

internal fun ByteArray.asBigEndianUInt() =
    asBigEndianInt().toUInt()

internal fun ByteArray.asBigEndianLong(): Long =
    ((this[0].toLong() and 0xff) shl 56) or
            ((this[1].toLong() and 0xff) shl 48) or
            ((this[2].toLong() and 0xff) shl 40) or
            ((this[3].toLong() and 0xff) shl 32) or
            ((this[4].toLong() and 0xff) shl 24) or
            ((this[5].toLong() and 0xff) shl 16) or
            ((this[6].toLong() and 0xff) shl 8) or
            (this[7].toLong() and 0xff)

internal fun ByteArray.asBigEndianULong() =
    asBigEndianLong().toULong()