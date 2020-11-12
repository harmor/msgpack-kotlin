package io.harmor.msgpack

import kotlin.LazyThreadSafetyMode.NONE

public class ArrayValue internal constructor(public val content: List<Value>)
    : Value, List<Value> by content {

    private val hashcode by lazy(NONE) {
        content.hashCode()
    }

    private val string by lazy(NONE) {
        content.toString()
    }

    override fun pack(packer: MessagePacker) {
        packer.beginArray(size)
        forEach { it.pack(packer) }
    }

    override fun equals(other: Any?): Boolean =
        other is ArrayValue && content == other.content

    override fun hashCode(): Int = hashcode

    override fun toString(): String = string
}

@MessagePackDslMarker
public class ArrayBuilder internal constructor(
        private val content: MutableList<Value> = ArrayList())
    : StructureScope {

    public val size: Int get() = content.size

    public fun append(value: Value) {
        content += value
    }

    public operator fun Value.unaryPlus(): Unit = append(this)

    public operator fun ByteArray.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun ShortArray.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun IntArray.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun LongArray.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun FloatArray.unaryPlus(): Unit = forEach { +float(it) }
    public operator fun DoubleArray.unaryPlus(): Unit = forEach { +float(it) }

    public operator fun Array<Byte>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<Short>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<Int>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<Long>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<Float>.unaryPlus(): Unit = forEach { +float(it) }
    public operator fun Array<Double>.unaryPlus(): Unit = forEach { +float(it) }

    public operator fun Boolean.unaryPlus(): Unit = +bool(this)
    public operator fun BooleanArray.unaryPlus(): Unit = forEach { +it }
    public operator fun Array<Boolean>.unaryPlus(): Unit = forEach { +it }

    public operator fun Array<UByte>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<UShort>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<UInt>.unaryPlus(): Unit = forEach { +int(it) }
    public operator fun Array<ULong>.unaryPlus(): Unit = forEach { +int(it) }

    public operator fun Nothing?.unaryPlus(): Unit = +nill()

    public operator fun String.unaryPlus(): Unit = +str(this)
    public operator fun Array<String>.unaryPlus(): Unit = forEach { +it }

    internal fun build(): ArrayValue = ArrayValue(content)
}


