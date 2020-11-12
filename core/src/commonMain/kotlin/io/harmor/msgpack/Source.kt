package io.harmor.msgpack

public interface Source {
    @Throws(IOException::class)
    public fun read(): Byte

    @Throws(IOException::class)
    public fun read(output: ByteArray, offset: Int = 0, len: Int = output.size)

    @Throws(IOException::class)
    public fun skip(bytes: Long)

    @Throws(IOException::class)
    public fun peek(): Byte

    @Throws(IOException::class)
    public fun close()

    public companion object
}

public fun Source.read(size: Int): ByteArray =
    ByteArray(size).also { read(it) }