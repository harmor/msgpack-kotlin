package io.harmor.msgpack

import java.io.InputStream

public class SourceInputStream(private val stream: InputStream) : Source {
    override fun read(): Byte {
        val byte = stream.read()
        if (byte == -1) throw EndOfSourceException()
        return byte.toByte()
    }

    override fun read(output: ByteArray, offset: Int, len: Int) {
        stream.read(output, offset, len).also {
            if (it < len) throw EndOfSourceException("expected $len bytes but got $it instead")
        }
    }

    override fun skip(bytes: Long) {
        stream.skip(bytes).also {
            if (it < bytes) throw EndOfSourceException()
        }
    }

    override fun peek(): Byte {
        check(stream.markSupported()) { "mark not supported: $stream" }
        stream.mark(1)
        try {
            return read()
        } finally {
            stream.reset()
        }
    }

    override fun close() {
        stream.close()
    }
}

public fun InputStream.toSource(): SourceInputStream = SourceInputStream(this)