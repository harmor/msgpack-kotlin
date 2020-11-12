package utils

internal fun ClosedRange<Int>.toLong() =
    start.toLong()..endInclusive.toLong()