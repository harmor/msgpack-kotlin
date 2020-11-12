package io.harmor.msgpack

public class ByteArraySink(size: Int = 32) : Sink {

    private var buf = ByteArray(size)

    public var size: Int = 0
        private set

    init {
        require(size > 0) { "Negative initial size: $size" }
    }

    override fun write(vararg bytes: Byte): Unit =
        write(bytes, 0, bytes.size)

    override fun write(bytes: ByteArray, offset: Int, len: Int) {
        require(offset >= 0) { "offset: $offset" }
        require(len >= 0) { "len: $len" }

        if (len == 0) return

        if (offset > bytes.size || offset + len > bytes.size) {
            throw IndexOutOfBoundsException()
        }

        ensureCapacity(size + len)

        bytes.copyInto(buf, size, offset, offset + len)
        size += len
    }

    override fun flush() {}

    override fun close() {}

    public fun reset() {
        size = 0
    }

    public fun toByteArray(): ByteArray {
        return buf.sliceArray(0 until size)
    }

    override fun toString(): String =
        buf.decodeToString(0, size)

    private fun ensureCapacity(minCapacity: Int) {
        if (minCapacity - buf.size > 0) grow(minCapacity)
    }

    private fun grow(minCapacity: Int) {
        val oldCapacity = buf.size
        var newCapacity = oldCapacity shl 1
        if (newCapacity - minCapacity < 0) newCapacity = minCapacity

        if (newCapacity < 0) {
            if (minCapacity < 0) throw RuntimeException("out of memory")
            newCapacity = Int.MAX_VALUE
        }

        buf = buf.copyOf(newCapacity)
    }
}