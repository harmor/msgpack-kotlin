package io.harmor.msgpack

import io.harmor.msgpack.internal.toHex
import kotlin.LazyThreadSafetyMode.NONE

public class BinaryValue internal constructor(public val bytes: ByteArray) : Value {

    private val hashcode by lazy(NONE) {
        bytes.contentHashCode()
    }

    private val string by lazy(NONE) {
        bytes.joinToString(prefix = "[", postfix = "]") { it.toHex() }
    }

    public val size: Int get() = bytes.size

    public operator fun get(position: Int): Byte = bytes[position]

    override fun pack(packer: MessagePacker) {
        packer += bytes
    }

    override fun equals(other: Any?): Boolean =
        other is BinaryValue && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = hashcode

    override fun toString(): String = string
}