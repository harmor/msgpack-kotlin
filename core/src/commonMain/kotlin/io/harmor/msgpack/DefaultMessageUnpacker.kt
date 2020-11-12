package io.harmor.msgpack

import io.harmor.msgpack.Value.Type
import io.harmor.msgpack.internal.from

public class DefaultMessageUnpacker(public val source: Source)
    : MessageUnpacker,
      StringUnpacker by DefaultStringUnpacker(source),
      BooleanUnpacker by DefaultBooleanUnpacker(source),
      NumberUnpacker by DefaultNumberUnpacker(source),
      BinaryUnpacker by DefaultBinaryUnpacker(source),
      MapUnpacker by DefaultMapUnpacker(source),
      ArrayUnpacker by DefaultArrayUnpacker(source),
      ExtensionUnpacker by DefaultExtensionUnpacker(source),
      NullUnpacker by DefaultNullUnpacker(source) {

    override fun getType(): Type = Type.from(source.peek())

    override fun hasNext(): Boolean = try {
        getType()
        true
    } catch (e: EndOfSourceException) {
        false
    }

    override fun read(bytes: ByteArray, offset: Int, size: Int) {
        source.read(bytes, offset, size)
    }

    override fun skip(bytes: Long) {
        source.skip(bytes)
    }
}