package io.harmor.msgpack.serialization

import io.harmor.common.toIntOrThrow
import io.harmor.decoder.MessageUnpacker
import io.harmor.decoder.nextBinary
import io.harmor.decoder.nextString
import io.harmor.decoder.skipElement
import io.harmor.dsl.Element.Type.NULL
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind.LIST
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder

private class MessagePackMapReader(msgpack: MessagePack, decoder: MessageUnpacker) :
    MessagePackReader(msgpack, decoder, decoder.nextMapSize().toIntOrThrow()) {

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = size
}

private class MessagePackListReader(msgpack: MessagePack, decoder: MessageUnpacker) :
    MessagePackReader(msgpack, decoder, decoder.nextArraySize().toIntOrThrow()) {

    private var index = 0

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = size

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (index == size) return CompositeDecoder.DECODE_DONE
        return index++
    }
}

internal open class MessagePackReader(protected val msgpack: MessagePack,
                                      protected val decoder: MessageUnpacker,
                                      protected open val size: Int = -1)
    : AbstractDecoder() {

    private var readProperties = 0

    override val serializersModule
        get() = msgpack.serializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            LIST, is PolymorphicKind -> MessagePackListReader(msgpack, decoder)
            else -> MessagePackMapReader(msgpack, decoder)
        }
    }

    override fun decodeNotNullMark() =
        decoder.nextType() != NULL

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return if (deserializer.descriptor == ByteArraySerializer().descriptor) {
            @Suppress("UNCHECKED_CAST")
            decoder.nextBinary().bytes as T
        } else {
            super.decodeSerializableValue(deserializer)
        }
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {

        while (true) {
            if (isDone()) return CompositeDecoder.DECODE_DONE
            val name = decoder.nextString().toString()
            readProperties++
            val index = descriptor.getElementIndex(name)

            if (index == CompositeDecoder.UNKNOWN_NAME) {
                if (msgpack.ignoreUnknownKeys)
                    decoder.skipElement()
                else
                    throw SerializationException("${descriptor.serialName} does not contain element with name '$name'." +
                            " You can enable 'MessagePack.ignoreUnknownKeys' property to ignore unknown keys")
            } else
                return index
        }
    }

    override fun decodeNull() = decoder.nextNull().let { null }
    override fun decodeBoolean() = decoder.nextBoolean().value

    override fun decodeChar() = decodeString().also {
        if (it.length != 1) throw SerializationException("expected char but got '$it'")
    }.first()

    override fun decodeString() = decoder.nextString().toString()
    override fun decodeByte() = decoder.nextByte()
    override fun decodeInt() = decoder.nextInt()
    override fun decodeShort() = decoder.nextShort()
    override fun decodeLong() = decoder.nextLong()
    override fun decodeFloat() = decoder.nextFloat()
    override fun decodeDouble() = decoder.nextDouble()

    protected open fun isDone() =
        decoder.hasNext() || (size in 0..readProperties)
}