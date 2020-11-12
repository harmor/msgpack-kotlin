package utils

import io.harmor.msgpack.Value
import io.harmor.msgpack.ByteArraySink

inline infix fun <reified E : Value> E.with(packer: MessagePackerFactory): TypedElement =
    packer().also {
        pack(it)
    }.let {
        (it.sink as ByteArraySink).toByteArray()
    }.let {
        TypedElement(it[0], it.unpack().convert())
    }
