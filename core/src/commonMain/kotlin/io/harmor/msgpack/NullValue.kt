package io.harmor.msgpack

public object NullValue : Value {
    override fun pack(packer: MessagePacker) {
        packer.pack(null)
    }

    override fun toString(): String = "nil"
}