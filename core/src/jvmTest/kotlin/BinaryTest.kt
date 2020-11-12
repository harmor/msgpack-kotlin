import io.harmor.msgpack.BinaryValue
import io.harmor.msgpack.internal.MessageType.BIN16
import io.harmor.msgpack.internal.MessageType.BIN32
import io.harmor.msgpack.internal.MessageType.BIN8
import io.harmor.msgpack.msgpack.bin
import io.harmor.msgpack.nextBinary
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory
import utils.TypedElement
import utils.byteArrays
import utils.pack
import utils.convert
import utils.with

class BinaryTest : AbstractMessagePackTest() {

    init {
        "Packer" should {

            "use bin8" {
                checkAll(Exhaustive.byteArrays(32..255)) {
                    it with packer shouldBe bin8(it)
                }
            }

            "use bin16" {
                checkAll(100, Arb.byteArrays(256..65535)) {
                    it with packer shouldBe bin16(it)
                }
            }

            "use bin32" {
                checkAll(100, Arb.byteArrays(65536..100_000)) {
                    it with packer shouldBe bin32(it)
                }
            }
        }

        "Unpacker" should {

            "decode bin8" {
                checkAll(100, Arb.byteArrays(32..255)) {
                    bin(it) with unpacker shouldBe bin(it)
                }
            }

            "decode bin16" {
                checkAll(100, Arb.byteArrays(256..65535)) {
                    bin(it) with unpacker shouldBe bin(it)
                }
            }

            "decode bin32" {
                checkAll(100, Arb.byteArrays(65536..100_000)) {
                    bin(it) with unpacker shouldBe bin(it)
                }
            }
        }
    }
}

private fun bin8(value: ByteArray) = TypedElement(BIN8, bin(value))
private fun bin16(value: ByteArray) = TypedElement(BIN16, bin(value))
private fun bin32(value: ByteArray) = TypedElement(BIN32, bin(value))

infix fun BinaryValue.with(unpacker: MessageUnpackerFactory): BinaryValue =
    unpacker(convert().pack()).nextBinary()

infix fun ByteArray.with(packerFactory: MessagePackerFactory): TypedElement =
    BinaryValue(this) with packerFactory

