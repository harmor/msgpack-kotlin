package io.harmor.msgpack;

public class DefaultMessagePacker(public val sink: Sink)
    : MessagePacker,
      NumberPacker by DefaultNumberPacker(sink),
      StringPacker by DefaultStringPacker(sink),
      MapPacker by DefaultMapPacker(sink),
      ArrayPacker by DefaultArrayPacker(sink),
      NullPacker by DefaultNullPacker(sink),
      BooleanPacker by DefaultBooleanPacker(sink),
      BinaryPacker by DefaultBinaryPacker(sink),
      ExtensionPacker by DefaultExtensionPacker(sink)