package io.harmor.msgpack

import kotlin.math.min

public class ByteArraySource(private val buffer: ByteArray, offset: Int = 0, length: Int = buffer.size) : Source {

    private var pos = offset
    private var count = min(offset + length, buffer.size)

    override fun peek(): Byte =
        if (pos < count)
            buffer[pos]
        else
            throw EndOfSourceException("expected 1 byte but ${available()} available")

    override fun close() {}

    override fun read(): Byte =
        if (pos < count)
            buffer[pos++]
        else
            throw EndOfSourceException("expected 1 byte but ${available()} available")

    override fun read(output: ByteArray, offset: Int, len: Int) {

        if (len == 0) return

        require(offset >= 0) { "offset: $offset" }
        require(len >= 0) { "len: $len" }
        require(offset + len <= output.size) {
            "offset + len must be less than or equals to output size"
        }

        val available = available()

        if (len > available) {
            throw EndOfSourceException("expected $len bytes but $available available")
        }

        buffer.copyInto(output, offset, pos, pos + len)

        pos += len
    }

    override fun skip(bytes: Long) {
        require(bytes >= 0) { "skip: $bytes" }
        val skipped = min(bytes, available().toLong())
        pos += skipped.toInt()
        if (skipped != bytes) throw EndOfSourceException()
    }

    private fun available(): Int {
        return count - pos
    }
}