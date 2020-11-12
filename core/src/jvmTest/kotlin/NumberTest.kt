import io.harmor.msgpack.NumberValue
import io.harmor.msgpack.internal.MessageType.FLOAT32
import io.harmor.msgpack.internal.MessageType.FLOAT64
import io.harmor.msgpack.internal.MessageType.INT16
import io.harmor.msgpack.internal.MessageType.INT32
import io.harmor.msgpack.internal.MessageType.INT64
import io.harmor.msgpack.internal.MessageType.INT8
import io.harmor.msgpack.internal.RANGE
import io.harmor.msgpack.msgpack.float
import io.harmor.msgpack.msgpack.int
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.bytes
import io.kotest.property.exhaustive.map
import io.kotest.property.exhaustive.plus
import utils.MessageUnpackerFactory
import utils.TypedElement
import utils.minus
import utils.pack
import utils.shorts
import utils.toLong
import utils.toShort
import utils.convert
import utils.ubytes
import utils.ushorts
import utils.with

private val ALL_NEGATIVE_FIX_INT = Exhaustive.bytes(-32, -1)
private val ALL_POSITIVE_FIX_INT = Exhaustive.bytes(0, 127)
private val ALL_FIX_INT = ALL_NEGATIVE_FIX_INT + ALL_POSITIVE_FIX_INT
private val ALL_INT8 = Exhaustive.bytes()
private val ALL_INT16 = Exhaustive.shorts()
private val ALL_UINT8 = Exhaustive.ubytes()
private val ALL_UINT16 = Exhaustive.ushorts()

class NumberTest : AbstractMessagePackTest() {

    init {
        "Packer" should {
            "use fixint" {
                checkAll(ALL_FIX_INT) {
                    int(it) with packer shouldBe fixint(it)
                    int(it.toShort()) with packer shouldBe fixint(it)
                    int(it.toInt()) with packer shouldBe fixint(it)
                    int(it.toLong()) with packer shouldBe fixint(it)
                }
            }

            "use int8" {
                checkAll(ALL_INT8 - ALL_FIX_INT) {
                    int(it) with packer shouldBe int8(it)
                    int(it.toShort()) with packer shouldBe int8(it)
                    int(it.toInt()) with packer shouldBe int8(it)
                    int(it.toLong()) with packer shouldBe int8(it)
                }
            }

            "use int16" {

                checkAll(ALL_INT16 - ALL_INT8.toShort()) {
                    int(it) with packer shouldBe int16(it)
                    int(it.toInt()) with packer shouldBe int16(it)
                    int(it.toLong()) with packer shouldBe int16(it)
                }
            }

            "use int32" {
                checkAll(Arb.int() - Short.RANGE) {
                    int(it) with packer shouldBe int32(it)
                    int(it.toLong()) with packer shouldBe int32(it)
                }
            }

            "use int64" {
                checkAll(Arb.long() - Int.RANGE.toLong()) {
                    int(it) with packer shouldBe int64(it)
                }
            }

            "use float32" {
                checkAll(Arb.float()) {
                    float(it) with packer shouldBe float32(it)
                }
            }

            "use float64" {
                checkAll(Arb.double()) {
                    float(it) with packer shouldBe float64(it)
                }
            }
        }

        "Unpacker" should {
            "decode fixint" {
                checkAll(ALL_FIX_INT) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode int8" {
                checkAll(ALL_INT8) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode int16" {
                checkAll(ALL_INT16) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode int32" {
                checkAll(Arb.int()) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode int64" {
                checkAll(Arb.long()) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode uint8" {
                checkAll(ALL_UINT8.map { it.toInt() }) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode uint16" {
                checkAll(ALL_UINT16.map { it.toInt() }) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode uint32" {
                checkAll(Arb.long(0L..UInt.MAX_VALUE.toLong())) {
                    int(it) with unpacker shouldBe int(it)
                }
            }

            "decode uint64" {
                checkAll(Arb.long()) {
                    int(it.toULong()) with unpacker shouldBe int(it.toULong())
                }
            }
        }
    }
}

private fun fixint(value: Byte) = TypedElement(value, int(value))
private fun int8(value: Byte) = TypedElement(INT8, int(value))
private fun int16(value: Short) = TypedElement(INT16, int(value))
private fun int32(value: Int) = TypedElement(INT32, int(value))
private fun int64(value: Long) = TypedElement(INT64, int(value))
private fun float32(value: Float) = TypedElement(FLOAT32, float(value.toDouble()))
private fun float64(value: Double) = TypedElement(FLOAT64, float(value))

infix fun NumberValue.with(unpacker: MessageUnpackerFactory): NumberValue =
    unpacker(convert().pack()).nextNumber()

