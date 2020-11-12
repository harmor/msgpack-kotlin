package io.harmor.msgpack.internal

internal fun Char.toBase16() = when (this) {
    in '0'..'9' -> this - '0'
    in 'A'..'F' -> this - 'A' + 10
    in 'a'..'f' -> this - 'a' + 10
    else -> throw IllegalArgumentException("invalid character: $this")
}

internal fun Byte.toHex(lowerCase: Boolean = false): String {
    val codes = if (lowerCase) "0123456789abcdef" else "0123456789ABCDEF"
    return buildString {
        append(codes[this@toHex.toInt() shr 4 and 0xF])
        append(codes[this@toHex.toInt() and 0xF])
    }
}

internal fun ByteArray.toHex(lowerCase: Boolean = false) = buildString(size * 2) {
    val codes = if (lowerCase) "0123456789abcdef" else "0123456789ABCDEF"
    this@toHex.forEach {
        append(codes[it.toInt() shr 4 and 0xF])
        append(codes[it.toInt() and 0xF])
    }
}

internal fun String.fromHex(): ByteArray {
    require(length % 2 == 0) { "string must be even length" }
    return ByteArray(length / 2) {
        val i = it * 2
        val msb = this[i].toBase16()
        val lsb = this[i + 1].toBase16()
        ((msb shl 4) + lsb).toByte()
    }
}