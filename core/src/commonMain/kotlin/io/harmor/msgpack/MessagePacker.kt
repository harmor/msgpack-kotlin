package io.harmor.msgpack

public interface MessagePacker : NumberPacker, StringPacker, MapPacker, ArrayPacker,
                                 NullPacker, BooleanPacker, BinaryPacker, ExtensionPacker

public fun MessagePacker.pack(value: Value): Unit =
    value.pack(this)


