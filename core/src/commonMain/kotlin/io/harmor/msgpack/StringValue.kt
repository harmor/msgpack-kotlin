package io.harmor.msgpack

import kotlin.LazyThreadSafetyMode.NONE

public class StringValue internal constructor(private val value: String) : Value {

    internal constructor(value: ByteArray) : this(value.decodeToString())

    private val hashcode by lazy(NONE) {
        bytes.contentHashCode()
    }

    public val bytes: ByteArray by lazy(NONE) {
        value.encodeToByteArray()
    }

    override fun pack(packer: MessagePacker) {
        packer.pack(value)
    }

    override fun equals(other: Any?): Boolean =
        other is StringValue && other.bytes.contentEquals(bytes)

    override fun hashCode(): Int = hashcode

    override fun toString(): String = value
}