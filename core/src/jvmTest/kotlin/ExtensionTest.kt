import io.harmor.msgpack.ExtensionValue
import io.harmor.msgpack.internal.MessageType.EXT16
import io.harmor.msgpack.internal.MessageType.EXT32
import io.harmor.msgpack.internal.MessageType.EXT8
import io.harmor.msgpack.internal.MessageType.FIXEXT1
import io.harmor.msgpack.internal.MessageType.FIXEXT16
import io.harmor.msgpack.internal.MessageType.FIXEXT2
import io.harmor.msgpack.internal.MessageType.FIXEXT4
import io.harmor.msgpack.internal.MessageType.FIXEXT8
import io.harmor.msgpack.nextExtension
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory
import utils.TypedElement
import utils.pack
import utils.convert
import utils.with
import kotlin.random.Random

class ExtensionTest : AbstractMessagePackTest() {

    init {
        "Packer" should {

            "use fixext1" {
                checkAll(Arb.extension(1)) {
                    it with packer shouldBe fixext1(it)
                }
            }

            "use fixext2" {
                checkAll(Arb.extension(2)) {
                    it with packer shouldBe fixext2(it)
                }
            }

            "use fixext4" {
                checkAll(Arb.extension(4)) {
                    it with packer shouldBe fixext4(it)
                }
            }

            "use fixext8" {
                checkAll(Arb.extension(8)) {
                    it with packer shouldBe fixext8(it)
                }
            }

            "use fixext16" {
                checkAll(Arb.extension(16)) {
                    it with packer shouldBe fixext16(it)
                }
            }

            "use ext8" {
                checkAll(Exhaustive.extension(17..255)) {
                    it with packer shouldBe ext8(it)
                }
            }

            "use ext16" {
                checkAll(Arb.extension(256..65535)) {
                    it with packer shouldBe ext16(it)
                }
            }

            "use ext32" {
                checkAll(Arb.extension(65536..100_000)) {
                    it with packer shouldBe ext32(it)
                }
            }
        }

        "Unpacker" should {

            "decode fixext1" {
                checkAll(Arb.extension(1)) {
                    it with unpacker shouldBe fixext1(it)
                }
            }

            "decode fixext2" {
                checkAll(Arb.extension(2)) {
                    it with unpacker shouldBe fixext2(it)
                }
            }

            "decode fixext4" {
                checkAll(Arb.extension(4)) {
                    it with unpacker shouldBe fixext4(it)
                }
            }

            "decode fixext8" {
                checkAll(Arb.extension(8)) {
                    it with unpacker shouldBe fixext8(it)
                }
            }

            "decode fixext16" {
                checkAll(Arb.extension(16)) {
                    it with unpacker shouldBe fixext16(it)
                }
            }

            "decode ext8" {
                checkAll(Arb.extension(17..255)) {
                    it with unpacker shouldBe ext8(it)
                }
            }

            "decode ext16" {
                checkAll(Arb.extension(256..65535)) {
                    it with unpacker shouldBe ext16(it)
                }
            }

            "decode ext32" {
                checkAll(Arb.extension(65536..100_000)) {
                    it with unpacker shouldBe ext32(it)
                }
            }
        }
    }
}

private fun Arb.Companion.extension(length: Int) =
    extension(length..length)

private fun Arb.Companion.extension(length: IntRange) = arbitrary {
    ExtensionValue(it.random.nextInt().toByte(), it.random.nextBytes(length.random(it.random)))
}

private fun Exhaustive.Companion.extension(length: IntRange) =
    length.map {
        ExtensionValue(Random.nextInt().toByte(), Random.nextBytes(it))
    }.toList().exhaustive()

private fun fixext1(extension: ExtensionValue) = TypedElement(FIXEXT1, extension)
private fun fixext2(extension: ExtensionValue) = TypedElement(FIXEXT2, extension)
private fun fixext4(extension: ExtensionValue) = TypedElement(FIXEXT4, extension)
private fun fixext8(extension: ExtensionValue) = TypedElement(FIXEXT8, extension)
private fun fixext16(extension: ExtensionValue) = TypedElement(FIXEXT16, extension)
private fun ext8(extension: ExtensionValue) = TypedElement(EXT8, extension)
private fun ext16(extension: ExtensionValue) = TypedElement(EXT16, extension)
private fun ext32(extension: ExtensionValue) = TypedElement(EXT32, extension)

private infix fun ExtensionValue.with(packerFactory: MessagePackerFactory): TypedElement =
    this with packerFactory

private infix fun ExtensionValue.with(unpacker: MessageUnpackerFactory): TypedElement =
    convert().pack().let {
        TypedElement(it[0], unpacker(it).nextExtension())
    }


