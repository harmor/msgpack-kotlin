package io.harmor.msgpack

public data class MapValue internal constructor(private val map: Map<Value, Value>)
    : Value, Map<Value, Value> by map {

    override fun pack(packer: MessagePacker) {
        packer.beginMap(map.size)
        map.forEach { (key, value) ->
            key.pack(packer)
            value.pack(packer)
        }
    }
}

@MessagePackDslMarker
public class MapBuilder internal constructor(
        private val map: MutableMap<Value, Value> = LinkedHashMap())
    : StructureScope {

    public val size: Int get() = map.size

    public fun put(key: Value, value: Value) {
        map[key] = value
    }

    public infix fun Value.with(value: Value): Unit = put(this, value)

    public infix fun String.with(value: Value): Unit = put(str(this), value)
    public infix fun String.with(value: String): Unit = put(str(this), str(value))
    public infix fun String.with(value: Boolean): Unit = put(str(this), bool(value))
    public infix fun String.with(value: Byte): Unit = put(str(this), int(value))
    public infix fun String.with(value: Short): Unit = put(str(this), int(value))
    public infix fun String.with(value: Int): Unit = put(str(this), int(value))
    public infix fun String.with(value: Long): Unit = put(str(this), int(value))
    public infix fun String.with(value: UByte): Unit = put(str(this), int(value))
    public infix fun String.with(value: UShort): Unit = put(str(this), int(value))
    public infix fun String.with(value: UInt): Unit = put(str(this), int(value))
    public infix fun String.with(value: ULong): Unit = put(str(this), int(value))
    public infix fun String.with(value: Float): Unit = put(str(this), float(value))
    public infix fun String.with(value: Double): Unit = put(str(this), float(value))
    public infix fun String.with(@Suppress("UNUSED_PARAMETER") value: Nothing?): Unit = put(str(this), nill())

    public infix fun Value.with(value: String): Unit = put(this, str(value))
    public infix fun Value.with(value: Boolean): Unit = put(this, bool(value))
    public infix fun Value.with(value: Byte): Unit = put(this, int(value))
    public infix fun Value.with(value: Short): Unit = put(this, int(value))
    public infix fun Value.with(value: Int): Unit = put(this, int(value))
    public infix fun Value.with(value: Long): Unit = put(this, int(value))
    public infix fun Value.with(value: UByte): Unit = put(this, int(value))
    public infix fun Value.with(value: UShort): Unit = put(this, int(value))
    public infix fun Value.with(value: UInt): Unit = put(this, int(value))
    public infix fun Value.with(value: ULong): Unit = put(this, int(value))
    public infix fun Value.with(value: Float): Unit = put(this, float(value))
    public infix fun Value.with(value: Double): Unit = put(this, float(value))
    public infix fun Value.with(@Suppress("UNUSED_PARAMETER") value: Nothing?): Unit = put(this, nill())

    internal fun build(): MapValue = MapValue(map)
}

public fun MapBuilder.append(entry: Pair<Value, Value>) {
    put(entry.first, entry.second)
}