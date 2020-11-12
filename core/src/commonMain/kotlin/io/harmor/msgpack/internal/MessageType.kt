package io.harmor.msgpack.internal

import io.harmor.msgpack.Value
import io.harmor.msgpack.Value.Type.ARRAY
import io.harmor.msgpack.Value.Type.BINARY
import io.harmor.msgpack.Value.Type.BOOLEAN
import io.harmor.msgpack.Value.Type.EXTENSION
import io.harmor.msgpack.Value.Type.FLOAT
import io.harmor.msgpack.Value.Type.INTEGER
import io.harmor.msgpack.Value.Type.MAP
import io.harmor.msgpack.Value.Type.STRING
import io.harmor.msgpack.internal.MessageType.ARRAY16
import io.harmor.msgpack.internal.MessageType.ARRAY32
import io.harmor.msgpack.internal.MessageType.BIN16
import io.harmor.msgpack.internal.MessageType.BIN32
import io.harmor.msgpack.internal.MessageType.BIN8
import io.harmor.msgpack.internal.MessageType.EXT16
import io.harmor.msgpack.internal.MessageType.EXT32
import io.harmor.msgpack.internal.MessageType.EXT8
import io.harmor.msgpack.internal.MessageType.FALSE
import io.harmor.msgpack.internal.MessageType.FIXARRAY
import io.harmor.msgpack.internal.MessageType.FIXEXT1
import io.harmor.msgpack.internal.MessageType.FIXEXT16
import io.harmor.msgpack.internal.MessageType.FIXEXT2
import io.harmor.msgpack.internal.MessageType.FIXEXT4
import io.harmor.msgpack.internal.MessageType.FIXEXT8
import io.harmor.msgpack.internal.MessageType.FIXMAP
import io.harmor.msgpack.internal.MessageType.FIXSTRING
import io.harmor.msgpack.internal.MessageType.FLOAT32
import io.harmor.msgpack.internal.MessageType.FLOAT64
import io.harmor.msgpack.internal.MessageType.INT16
import io.harmor.msgpack.internal.MessageType.INT32
import io.harmor.msgpack.internal.MessageType.INT64
import io.harmor.msgpack.internal.MessageType.INT8
import io.harmor.msgpack.internal.MessageType.MAP16
import io.harmor.msgpack.internal.MessageType.MAP32
import io.harmor.msgpack.internal.MessageType.NEGATIVE_FIXINT
import io.harmor.msgpack.internal.MessageType.NULL
import io.harmor.msgpack.internal.MessageType.POSITIVE_FIXINT
import io.harmor.msgpack.internal.MessageType.STRING16
import io.harmor.msgpack.internal.MessageType.STRING32
import io.harmor.msgpack.internal.MessageType.STRING8
import io.harmor.msgpack.internal.MessageType.TRUE
import io.harmor.msgpack.internal.MessageType.UINT16
import io.harmor.msgpack.internal.MessageType.UINT32
import io.harmor.msgpack.internal.MessageType.UINT64
import io.harmor.msgpack.internal.MessageType.UINT8
import io.harmor.msgpack.internal.MessageType.UNUSED

internal object MessageType {
    internal const val NULL = 0xC0.toByte()

    internal const val UNUSED = 0xC1.toByte()

    internal const val FALSE = 0xC2.toByte()
    internal const val TRUE = 0xC3.toByte()

    internal const val BIN8 = 0xC4.toByte()
    internal const val BIN16 = 0xC5.toByte()
    internal const val BIN32 = 0xC6.toByte()

    internal const val EXT8 = 0xC7.toByte()
    internal const val EXT16 = 0xC8.toByte()
    internal const val EXT32 = 0xC9.toByte()

    internal const val FLOAT32 = 0xCA.toByte()
    internal const val FLOAT64 = 0xCB.toByte()

    internal const val UINT8 = 0xCC.toByte()
    internal const val UINT16 = 0xCD.toByte()
    internal const val UINT32 = 0xCE.toByte()
    internal const val UINT64 = 0xCF.toByte()

    internal const val INT8 = 0xD0.toByte()
    internal const val INT16 = 0xD1.toByte()
    internal const val INT32 = 0xD2.toByte()
    internal const val INT64 = 0xD3.toByte()

    internal const val FIXEXT1 = 0xD4.toByte()
    internal const val FIXEXT2 = 0xD5.toByte()
    internal const val FIXEXT4 = 0xD6.toByte()
    internal const val FIXEXT8 = 0xD7.toByte()
    internal const val FIXEXT16 = 0xD8.toByte()

    internal const val STRING8 = 0xD9.toByte()
    internal const val STRING16 = 0xDA.toByte()
    internal const val STRING32 = 0xDB.toByte()

    internal const val ARRAY16 = 0xDC.toByte()
    internal const val ARRAY32 = 0xDD.toByte()

    internal const val MAP16 = 0xDE.toByte()
    internal const val MAP32 = 0xDF.toByte()

    internal val POSITIVE_FIXINT = 0x00.toByte()..0x7f.toByte()
    internal val FIXMAP = 0x80.toByte()..0x8f.toByte()
    internal val FIXARRAY = 0x90.toByte()..0x9f.toByte()
    internal val FIXSTRING = 0xa0.toByte()..0xbf.toByte()
    internal val NEGATIVE_FIXINT = 0xe0.toByte()..0xff.toByte()

    internal fun nameOf(value: Byte): String = when (value) {
        NULL -> "null"
        TRUE -> "true"
        FALSE -> "false"

        BIN8 -> "bin8"
        BIN16 -> "bin16"
        BIN32 -> "bin32"

        EXT8 -> "ext8"
        EXT16 -> "ext16"
        EXT32 -> "ext32"

        FLOAT32 -> "float32"
        FLOAT64 -> "float64"

        UINT8 -> "uint8"
        UINT16 -> "uint16"
        UINT32 -> "uint32"
        UINT64 -> "uint64"

        INT8 -> "int8"
        INT16 -> "int16"
        INT32 -> "int32"
        INT64 -> "int64"

        FIXEXT1 -> "fixext1"
        FIXEXT2 -> "fixext2"
        FIXEXT4 -> "fixext4"
        FIXEXT8 -> "fixext8"
        FIXEXT16 -> "fixext16"

        STRING8 -> "str8"
        STRING16 -> "str16"
        STRING32 -> "str32"

        ARRAY16 -> "array16"
        ARRAY32 -> "array32"

        MAP16 -> "map16"
        MAP32 -> "map32"

        in POSITIVE_FIXINT -> "positive fixint"
        in NEGATIVE_FIXINT -> "negative fixint"
        in FIXSTRING -> "fixstr"
        in FIXARRAY -> "fixarray"
        in FIXMAP -> "fixmap"

        else -> "0x${value.toHex()}"
    }
}

internal fun Value.Type.Companion.from(value: Byte): Value.Type = when (value) {
    NULL -> Value.Type.NULL
    TRUE, FALSE -> BOOLEAN
    BIN8, BIN16, BIN32 -> BINARY
    FLOAT32, FLOAT64 -> FLOAT

    INT8, INT16, INT32, INT64 -> INTEGER
    UINT8, UINT16, UINT32, UINT64 -> INTEGER

    FIXEXT1, FIXEXT2, FIXEXT4, FIXEXT8, FIXEXT16 -> EXTENSION
    EXT8, EXT16, EXT32 -> EXTENSION

    STRING8, STRING16, STRING32 -> STRING

    ARRAY16, ARRAY32 -> ARRAY

    MAP16, MAP32 -> MAP
    UNUSED -> Value.Type.UNUSED

    in POSITIVE_FIXINT -> INTEGER
    in NEGATIVE_FIXINT -> INTEGER
    in FIXSTRING -> STRING
    in FIXARRAY -> ARRAY
    in FIXMAP -> MAP

    else -> throw IllegalStateException("cannot be there")
}


