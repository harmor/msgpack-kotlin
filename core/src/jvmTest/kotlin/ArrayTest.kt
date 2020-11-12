import io.harmor.msgpack.IntegerValue
import io.harmor.msgpack.ByteArraySink
import io.harmor.msgpack.internal.MessageType.ARRAY16
import io.harmor.msgpack.internal.MessageType.ARRAY32
import io.harmor.msgpack.msgpack
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.ints
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory
import utils.TypedElement

class ArrayTest : AbstractMessagePackTest() {

    init {
        "Packer" should {

            "use fixarray" {
                checkAll(Exhaustive.ints(0..15)) {
                    it with packer shouldBe fixarray(it)
                }
            }

            "use array16" {
                checkAll(Arb.int(16..Short.MAX_VALUE)) {
                    it with packer shouldBe array16(it)
                }
            }

            "use array32" {
                checkAll(Arb.int(65536..Int.MAX_VALUE)) {
                    it with packer shouldBe array32(it)
                }
            }
        }

        "Unpacker" should {

            "decode fixarray" {
                checkAll(Exhaustive.ints(0..15)) {
                    it with unpacker shouldBe fixarray(it)
                }
            }

            "decode array16" {
                checkAll(Arb.int(16..Short.MAX_VALUE)) {
                    it with unpacker shouldBe array16(it)
                }
            }

            "decode array32" {
                checkAll(Arb.int(65536..Int.MAX_VALUE)) {
                    it with unpacker shouldBe array32(it)
                }
            }
        }
    }
}

private fun fixarray(size: Int) = TypedElement((0x90 or size).toByte(), IntegerValue.from(size))
private fun array16(size: Int) = TypedElement(ARRAY16, IntegerValue.from(size))
private fun array32(size: Int) = array32(size.toUInt())
private fun array32(size: UInt) = TypedElement(ARRAY32, IntegerValue.from(size))

private infix fun Int.with(packer: MessagePackerFactory): TypedElement =
    packer().let {
        it.beginArray(toUInt())
        (it.sink as ByteArraySink).toByteArray()
    }.let {
        val unpacker = org.msgpack.core.MessagePack.newDefaultUnpacker(it)
        TypedElement(it[0], IntegerValue.from(unpacker.unpackArrayHeader()))
    }

private infix fun Int.with(unpacker: MessageUnpackerFactory) =
    org.msgpack.core.MessagePack.newDefaultBufferPacker().use { packer ->
        packer.packArrayHeader(this) // FIXME: maximum size is Int.MAX_VALUE
        packer
    }.toByteArray().let {
        TypedElement(it[0], msgpack.int(unpacker(it).nextArraySize()))
    }
    



