package utils

import io.harmor.msgpack.internal.MessageType.nameOf
import io.harmor.msgpack.MessageUnpacker
import io.harmor.msgpack.Value
import io.harmor.msgpack.DefaultMessagePacker
import java.util.Objects

typealias MessagePackerFactory = () -> DefaultMessagePacker
typealias MessageUnpackerFactory = (ByteArray) -> MessageUnpacker

class TypedElement(val type: Byte, val value: Value) {

    override fun equals(other: Any?) =
        other is TypedElement && type == other.type && value == other.value

    override fun hashCode() =
        Objects.hash(type, value)

    override fun toString() = "${nameOf(type)}($value)"
}