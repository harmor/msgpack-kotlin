package utils

import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArrays
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.merge
import kotlin.random.nextUInt
import kotlin.random.nextULong

operator fun <A> Arb<A>.plus(other: Arb<A>) =
    merge(other)

operator fun <A> Arb<A>.minus(other: Exhaustive<A>) =
    filter { it !in other.values }

operator fun <V : Comparable<V>> Arb<V>.minus(range: ClosedRange<V>) =
    filter { it !in range }

fun Arb.Companion.byteArrays(length: Int) =
    byteArrays(length..length)

fun Arb.Companion.byteArrays(length: IntRange) =
    Arb.byteArrays(Arb.int(length), Arb.byte())

fun Arb.Companion.uint(range: UIntRange = UInt.MIN_VALUE..UInt.MAX_VALUE) = arbitrary(listOf(range.first, range.last)) {
    it.random.nextUInt(range)
}

fun Arb.Companion.ulong() = arbitrary(listOf(ULong.MIN_VALUE, ULong.MAX_VALUE)) {
    it.random.nextULong()
}

inline fun <reified T> Arb.Companion.arrayOf(value: Arb<T>, length: IntRange): Arb<Array<T>> = arbitrary { rs ->
    Array(rs.random.nextInt(length.first, length.last)) {
        value.sample(rs).value
    }
}