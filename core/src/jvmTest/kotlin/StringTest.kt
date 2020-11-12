import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.arabic
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.asString
import io.kotest.property.arbitrary.ascii
import io.kotest.property.arbitrary.egyptianHieroglyphs
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.take
import io.kotest.property.checkAll
import io.harmor.msgpack.internal.MessageType.FIXSTRING
import io.harmor.msgpack.internal.MessageType.STRING16
import io.harmor.msgpack.internal.MessageType.STRING32
import io.harmor.msgpack.internal.MessageType.STRING8
import io.harmor.msgpack.nextString
import io.harmor.msgpack.StringValue
import io.harmor.msgpack.msgpack.str
import utils.TypedElement
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory
import utils.bytes
import utils.pack
import utils.plus
import utils.convert
import utils.with
import kotlin.experimental.or
import kotlin.random.nextInt

class StringTest : AbstractMessagePackTest() {

    init {
        "Packer" should {
            "use fixstr" {
                checkAll(byteString(0..31)) {
                    it with packer shouldBe fixstr(it)
                }
            }

            "use str8" {
                checkAll(byteString(32..255)) {
                    it with packer shouldBe str8(it)
                }
            }

            "use str16" {
                checkAll(100, byteString(256..65535)) {
                    it with packer shouldBe str16(it)
                }
            }

            "use str32" {
                checkAll(100, byteString(65536..100_000)) {
                    it with packer shouldBe str32(it)
                }
            }
        }

        "Unpacker" should {
            "decode fixstr" {
                checkAll(byteString(0..31)) {
                    str(it) with unpacker shouldBe str(it)
                }
            }

            "decode str8" {
                checkAll(100, byteString(32..255)) {
                    str(it) with unpacker shouldBe str(it)
                }
            }

            "decode str16" {
                checkAll(100, byteString(256..65535)) {
                    str(it) with unpacker shouldBe str(it)
                }
            }

            "decode str32" {
                checkAll(100, byteString(65536..100_000)) {
                    str(it) with unpacker shouldBe str(it)
                }
            }

            "decode utf-8" {
                checkAll(100, Arb.string(0..100_000, Arb.arabic() + Arb.egyptianHieroglyphs())) {
                    str(it) with unpacker shouldBe str(it)
                }
            }
        }
    }
}

private fun byteString(bytes: IntRange, codepoint: Arb<Codepoint> = Arb.ascii()): Arb<String> {

    val edgeCases = listOf(codepoint.toString(bytes.first), codepoint.toString(bytes.last))

    return arbitrary(edgeCases) { rs ->
        val count = rs.random.nextInt(bytes)
        codepoint.toString(count, rs)
    }
}

fun Arb<Codepoint>.toString(count: Int, rs: RandomSource = RandomSource.Default) =
    take(count, rs).joinToString("") { it.asString() }

private fun fixstr(value: String) = TypedElement(FIXSTRING.first.toByte() or value.bytes.toByte(), str(value))
private fun str8(value: String) = TypedElement(STRING8, str(value))
private fun str16(value: String) = TypedElement(STRING16, str(value))
private fun str32(value: String) = TypedElement(STRING32, str(value))

infix fun StringValue.with(unpacker: MessageUnpackerFactory): StringValue =
    unpacker(convert().pack()).nextString()

infix fun String.with(packerFactory: MessagePackerFactory): TypedElement =
    StringValue(this) with packerFactory

