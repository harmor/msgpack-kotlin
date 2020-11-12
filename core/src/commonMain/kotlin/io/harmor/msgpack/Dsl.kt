package io.harmor.msgpack

@DslMarker
internal annotation class MessagePackDslMarker

public interface StructureScope {

    public fun array(values: Array<Value>): ArrayValue =
        ArrayValue(values.toList())

    public fun array(builder: ArrayBuilder.() -> Unit): ArrayValue =
        ArrayBuilder().apply(builder).build()

    public fun bin(value: ByteArray): BinaryValue =
        BinaryValue(value)

    public fun bool(value: Boolean): BooleanValue =
        if (value) BooleanValue.TRUE else BooleanValue.FALSE

    public fun ext(type: Byte, vararg bytes: Byte): ExtensionValue =
        ExtensionValue(type, bytes)

    public fun ext(type: Byte, builder: ExtensionBuilder.() -> Unit): ExtensionValue =
        ExtensionBuilder(type).apply { builder() }.build()

    public fun map(vararg elements: Pair<Value, Value>): MapValue = map {
        elements.forEach { append(it) }
    }

    public fun map(builder: MapBuilder.() -> Unit): MapValue =
        MapBuilder().apply(builder).build()

    public fun nill(): NullValue = NullValue

    public fun int(value: Byte): IntegerValue = IntegerValue.from(value)
    public fun int(value: Short): IntegerValue = IntegerValue.from(value)
    public fun int(value: Int): IntegerValue = IntegerValue.from(value)
    public fun int(value: Long): IntegerValue = IntegerValue.from(value)
    public fun int(value: UByte): IntegerValue = IntegerValue.from(value)
    public fun int(value: UShort): IntegerValue = IntegerValue.from(value)
    public fun int(value: UInt): IntegerValue = IntegerValue.from(value)
    public fun int(value: ULong): IntegerValue = IntegerValue.from(value)
    public fun float(value: Float): FloatValue = FloatValue(value)
    public fun float(value: Double): FloatValue = FloatValue(value)

    public fun str(value: String): StringValue = StringValue(value)
    public fun str(value: ByteArray): StringValue = StringValue(value)
}

@Suppress("ClassName")
@MessagePackDslMarker
public object msgpack : StructureScope

public fun msgpack(builder: MapBuilder.() -> Unit): MapValue = msgpack.map(builder)





