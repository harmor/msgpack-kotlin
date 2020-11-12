package io.harmor.msgpack

import java.io.OutputStream

public class SinkOutputStream(private val stream: OutputStream) : Sink {
    override fun write(vararg bytes: Byte) {
        return stream.write(bytes)
    }

    override fun write(bytes: ByteArray, offset: Int, len: Int) {
        return stream.write(bytes, offset, len)
    }

    override fun flush() {
        stream.flush()
    }

    override fun close() {
        stream.close()
    }
}

public fun OutputStream.toSink(): SinkOutputStream = SinkOutputStream(this)