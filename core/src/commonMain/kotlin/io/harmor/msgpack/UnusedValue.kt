package io.harmor.msgpack

import io.harmor.msgpack.internal.MessageType.UNUSED

public object UnusedValue : Value {
    override fun pack(packer: MessagePacker) {
        packer.appendBinary(UNUSED)
    }
}