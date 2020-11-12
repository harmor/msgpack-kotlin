package io.harmor.msgpack

public interface Sink {
    @Throws(IOException::class)
    public fun write(vararg bytes: Byte)

    @Throws(IOException::class)
    public fun write(bytes: ByteArray, offset: Int = 0, len: Int = bytes.size)

    @Throws(IOException::class)
    public fun flush()

    @Throws(IOException::class)
    public fun close()
}

internal operator fun Sink.plusAssign(value: Byte) = write(value)
internal operator fun Sink.plusAssign(value: ByteArray) = write(value)