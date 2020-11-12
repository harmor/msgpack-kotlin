import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.ints
import io.harmor.msgpack.internal.MessageType.MAP16
import io.harmor.msgpack.internal.MessageType.MAP32
import io.harmor.msgpack.IntegerValue
import io.harmor.msgpack.MapValue
import io.harmor.msgpack.ByteArraySink
import io.harmor.msgpack.msgpack
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory
import utils.TypedElement
import utils.with

class MapTest : AbstractMessagePackTest() {

    init {
        "Packer" should {

            "use fixmap" {
                checkAll(Exhaustive.ints(0..15)) {
                    it with packer shouldBe fixmap(it)
                }
            }

            "use map16" {
                checkAll(Arb.int(16..Short.MAX_VALUE)) {
                    it with packer shouldBe map16(it)
                }
            }

            "use map32" {
                checkAll(Arb.int(65536..Int.MAX_VALUE)) {
                    it with packer shouldBe map32(it)
                }
            }

            "encode null key" {
                val m = msgpack { nill() with "string" }
                m with packer shouldBe fixmap(m)
            }

            "encode null value" {
                val m = msgpack { "key" with null }
                m with packer shouldBe fixmap(m)
            }
        }

        "Unpacker" should {

            "decode fixmap" {
                checkAll(Exhaustive.ints(0..15)) {
                    it with unpacker shouldBe fixmap(it)
                }
            }

            "decode map16" {
                checkAll(Arb.int(16..Short.MAX_VALUE)) {
                    it with unpacker shouldBe map16(it)
                }
            }

            "decode map32" {
                checkAll(Arb.int(65536..Int.MAX_VALUE)) {
                    it with unpacker shouldBe map32(it)
                }
            }
        }
    }
}

private fun fixmap(element: MapValue) = TypedElement((0x80 or element.size).toByte(), element)

private fun fixmap(size: Int) = TypedElement((0x80 or size).toByte(), IntegerValue.from(size))
private fun map16(size: Int) = TypedElement(MAP16, IntegerValue.from(size))
private fun map32(size: Int) = map32(size.toUInt())
private fun map32(size: UInt) = TypedElement(MAP32, IntegerValue.from(size))

private infix fun Int.with(packer: MessagePackerFactory): TypedElement =
    packer().let {
        it.beginMap(toUInt())
        (it.sink as ByteArraySink).toByteArray()
    }.let {
        val unpacker = org.msgpack.core.MessagePack.newDefaultUnpacker(it)
        TypedElement(it[0], IntegerValue.from(unpacker.unpackMapHeader()))
    }

private infix fun Int.with(unpacker: MessageUnpackerFactory) =
    org.msgpack.core.MessagePack.newDefaultBufferPacker().use { packer ->
        packer.packMapHeader(this) // FIXME: maximum size is Int.MAX_VALUE
        packer
    }.toByteArray().let {
        TypedElement(it[0], msgpack.int(unpacker(it).nextMapSize()))
    }
    



