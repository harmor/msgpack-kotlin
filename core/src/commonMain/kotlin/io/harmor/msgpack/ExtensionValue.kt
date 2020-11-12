package io.harmor.msgpack

public data class ExtensionValue internal constructor(public val type: Byte, public val bytes: ByteArray) : Value {
    public val size: Int get() = bytes.size
    public operator fun get(position: Int): Byte = bytes[position]

    override fun pack(packer: MessagePacker) {
        packer.beginExtension(type, bytes.size)
        packer.appendExtension(bytes)
    }

    override fun equals(other: Any?): Boolean =
        other is ExtensionValue && type == other.type && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int =
        31 * type + bytes.contentHashCode()
}

@MessagePackDslMarker
public class ExtensionBuilder internal constructor(private val type: Byte, private val output: ByteArraySink = ByteArraySink()) {

    public fun append(vararg bytes: Byte) {
        append(bytes)
    }

    public fun append(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size) {
        output.write(bytes, offset, size)
    }

    internal fun build(): ExtensionValue = ExtensionValue(type, output.toByteArray())
}