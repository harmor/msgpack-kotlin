package io.harmor.msgpack.serialization

import io.harmor.common.ByteArrayInputStream
import io.harmor.common.ByteArrayOutputStream
import io.harmor.common.ByteInputStream
import io.harmor.common.DefaultByteArrayOutputStream
import io.harmor.decoder.DefaultMessageUnpacker
import io.harmor.decoder.MessageUnpacker
import io.harmor.encoder.CanonicalNumberEncoder
import io.harmor.encoder.DefaultMessagePacker
import io.harmor.encoder.MessagePacker
import io.harmor.serialization.MessagePackBuilder.IntegerSerializationStrategy
import io.harmor.serialization.MessagePackBuilder.IntegerSerializationStrategy.CANONICAL
import io.harmor.serialization.MessagePackBuilder.IntegerSerializationStrategy.SMALLEST
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

private val defaultEncoderFactory: MessagePackEncoderFactory = {
    DefaultMessagePacker(it)
}

private val defaultOutputStreamFactory: OutputStreamFactory = {
    DefaultByteArrayOutputStream()
}

private val canonicalEncoderFactory: MessagePackEncoderFactory = {
    DefaultMessagePacker(it, CanonicalNumberEncoder(it))
}

private val defaultDecoderFactory: MessagePackDecoderFactory = {
    DefaultMessageUnpacker(it)
}

public sealed class MessagePack(internal val encoderFactory: MessagePackEncoderFactory,
                                internal val decoderFactory: MessagePackDecoderFactory,
                                internal val outputStreamFactory: OutputStreamFactory,
                                internal val ignoreUnknownKeys: Boolean,
                                override val serializersModule: SerializersModule) : BinaryFormat {

    public companion object Default :
        MessagePack(defaultEncoderFactory, defaultDecoderFactory, defaultOutputStreamFactory, false, EmptySerializersModule)

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        return outputStreamFactory().also {
            val writer = encoderFactory(it)
            serializer.serialize(MessagePackWriter(this@MessagePack, writer), value)
        }.toByteArray()
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val decoder = decoderFactory(ByteArrayInputStream(bytes))
        val reader = MessagePackReader(this@MessagePack, decoder)
        return reader.decodeSerializableValue(deserializer)
    }
}

private class MessagePackImpl(builder: MessagePackBuilder) :
    MessagePack(builder.encoderFactory, builder.decoderFactory, builder.outputStreamFactory,
            builder.ignoreUnknownKeys, builder.serializersModule)

public fun MessagePack(from: MessagePack = MessagePack, builderAction: MessagePackBuilder.() -> Unit): MessagePack {
    val builder = MessagePackBuilder(from).apply { builderAction() }
    return MessagePackImpl(builder)
}

public typealias OutputStreamFactory = () -> ByteArrayOutputStream
public typealias MessagePackEncoderFactory = (ByteArrayOutputStream) -> MessagePacker
public typealias MessagePackDecoderFactory = (ByteInputStream) -> MessageUnpacker

public class MessagePackBuilder(msgPack: MessagePack) {

    public enum class IntegerSerializationStrategy {
        CANONICAL, SMALLEST
    }

    public var encoderFactory: MessagePackEncoderFactory = msgPack.encoderFactory
    public var decoderFactory: MessagePackDecoderFactory = msgPack.decoderFactory
    public var outputStreamFactory: OutputStreamFactory = msgPack.outputStreamFactory
    public var ignoreUnknownKeys: Boolean = msgPack.ignoreUnknownKeys
    public var serializersModule: SerializersModule = msgPack.serializersModule
}

public fun MessagePackBuilder.setIntegerSerializationStrategy(strategy: IntegerSerializationStrategy) {
    encoderFactory = when (strategy) {
        SMALLEST -> defaultEncoderFactory
        CANONICAL -> canonicalEncoderFactory
    }
}