package utils

import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.Gen
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.next
import io.kotest.property.exhaustive.exhaustive
import io.kotest.property.exhaustive.filter
import io.kotest.property.exhaustive.ints
import io.kotest.property.exhaustive.map

fun <N : Number> Exhaustive<N>.toInt() = map { it.toInt() }
fun <N : Number> Exhaustive<N>.toShort() = map { it.toShort() }

fun Exhaustive.Companion.shorts(min: Short = Short.MIN_VALUE, max: Short = Short.MAX_VALUE) =
    Exhaustive.ints(min..max).map { it.toShort() }

fun Exhaustive.Companion.ubytes(min: UByte = UByte.MIN_VALUE, max: UByte = UByte.MAX_VALUE): Exhaustive<UByte> =
    Exhaustive.ints(min.toInt()..max.toInt()).map { it.toUByte() }

fun Exhaustive.Companion.ushorts(min: UShort = UShort.MIN_VALUE, max: UShort = UShort.MAX_VALUE): Exhaustive<UShort>  =
    Exhaustive.ints(min.toInt()..max.toInt()).map { it.toUShort() }

fun Exhaustive.Companion.byteArrays(length: IntRange, byte: Gen<Byte> = Arb.byte()): Exhaustive<ByteArray> {
    val generator = byte.generate(RandomSource.Default).iterator()
    return length.map { ByteArray(it) { generator.next().value } }.exhaustive()
}

operator fun <A> Exhaustive<A>.minus(other: Exhaustive<A>) =
    filter { it !in other.values }

inline fun <reified T> Exhaustive.Companion.arrayOf(value: Arb<T>, length: IntRange): Exhaustive<Array<T>> {
    return length.map { Array(it) { value.next() } }.exhaustive()
}
