package io.harmor.msgpack.serialization

import io.harmor.encoder.MessagePacker
import io.harmor.encoder.beginArray
import io.harmor.encoder.beginBinary
import io.harmor.encoder.beginMap
import io.harmor.encoder.pack
import io.harmor.encoder.plusAssign
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind.LIST
import kotlinx.serialization.descriptors.StructureKind.MAP
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule

private class MsgPackMapWriter(msgpack: MessagePack, writer: MessagePacker)
    : MessagePackWriter(msgpack, writer) {

    override fun writeBeginStructure(size: Int) = encoder.beginMap(size)

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        encoder.pack(descriptor.getElementName(index))
        return true
    }
}

private class MsgPackListWriter(msgpack: MessagePack, writer: MessagePacker) :
    MessagePackWriter(msgpack, writer) {

    override fun writeBeginStructure(size: Int) = encoder.beginArray(size)
}

private class MsgPackBinaryWriter(msgpack: MessagePack, writer: MessagePacker) :
    MessagePackWriter(msgpack, writer) {

    override fun writeBeginStructure(size: Int) = encoder.beginBinary(size)

    override fun encodeByte(value: Byte) {
        encoder.appendBinary(value)
    }

    override fun encodeBoolean(value: Boolean) = throw IllegalStateException()
    override fun encodeChar(value: Char) = throw IllegalStateException()
    override fun encodeShort(value: Short) = throw IllegalStateException()
    override fun encodeInt(value: Int) = throw IllegalStateException()
    override fun encodeLong(value: Long) = throw IllegalStateException()
    override fun encodeFloat(value: Float) = throw IllegalStateException()
    override fun encodeDouble(value: Double) = throw IllegalStateException()
    override fun encodeString(value: String) = throw IllegalStateException()
}

internal open class MessagePackWriter(private val msgpack: MessagePack, protected val encoder: MessagePacker)
    : AbstractEncoder() {

    override val serializersModule: SerializersModule
        get() = msgpack.serializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind) {
            LIST, is PolymorphicKind -> MsgPackListWriter(msgpack, encoder)
            MAP -> MsgPackMapWriter(msgpack, encoder)
            else -> return super.beginStructure(descriptor)
        }.also {
            it.writeBeginStructure(descriptor.elementsCount)
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return when {
            descriptor == ByteArraySerializer().descriptor -> MsgPackBinaryWriter(msgpack, encoder)
            descriptor.kind == LIST -> MsgPackListWriter(msgpack, encoder)
            descriptor.kind == MAP -> MsgPackMapWriter(msgpack, encoder)
            else -> return super.beginCollection(descriptor, collectionSize)
        }.also {
            it.writeBeginStructure(collectionSize)
        }
    }

    protected open fun writeBeginStructure(size: Int): Unit =
        throw IllegalStateException()

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        if (serializer.descriptor == ByteArraySerializer().descriptor)
            encoder += value as ByteArray
        else
            super.encodeSerializableValue(serializer, value)
    }

    override fun encodeNull() = encoder.pack(null)
    override fun encodeBoolean(value: Boolean) = encoder.pack(value)
    override fun encodeByte(value: Byte) = encoder.pack(value)
    override fun encodeChar(value: Char) = encoder.pack(value)
    override fun encodeShort(value: Short) = encoder.pack(value)
    override fun encodeInt(value: Int) = encoder.pack(value)
    override fun encodeLong(value: Long) = encoder.pack(value)
    override fun encodeFloat(value: Float) = encoder.pack(value)
    override fun encodeDouble(value: Double) = encoder.pack(value)
    override fun encodeString(value: String) = encoder.pack(value)
}

