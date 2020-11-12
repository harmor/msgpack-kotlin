package io.harmor.msgpack

public interface Value {
    public enum class Type {
        ARRAY, BINARY, BOOLEAN, EXTENSION, FLOAT, INTEGER, MAP, NULL, STRING, UNUSED;

        internal companion object
    }

    public fun pack(packer: MessagePacker)
}