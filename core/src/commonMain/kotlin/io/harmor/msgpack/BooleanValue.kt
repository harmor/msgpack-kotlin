package io.harmor.msgpack

public class BooleanValue internal constructor(public val value: Boolean) : Value {

    override fun pack(packer: MessagePacker) {
        packer += value
    }

    override fun equals(other: Any?): Boolean =
        other is BooleanValue && other.value == value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    public companion object {
        public val TRUE: BooleanValue = BooleanValue(true)
        public val FALSE: BooleanValue = BooleanValue(false)
    }
}


