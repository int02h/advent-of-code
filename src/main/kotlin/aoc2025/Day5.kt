package aoc2025

import AocDay2
import Input
import kotlin.math.max
import kotlin.math.min

class Day5 : AocDay2 {

    private val freshRanges = mutableListOf<LongRange>()
    private val available = mutableListOf<Long>()

    override fun readInput(input: Input) {
        var isReadingFresh = true
        input.asLines().forEach { line ->
            if (isReadingFresh) {
                if (line == "") {
                    isReadingFresh = false
                } else {
                    val (start, end) = line.split("-").map { it.toLong() }
                    freshRanges += start..end
                }
            } else {
                available += line.toLong()
            }
        }
    }

    override fun part1() {
        var freshCount = 0
        available.forEach { id ->
            if (freshRanges.any { it.contains(id) }) freshCount++
        }
        println(freshCount)
    }

    override fun part2() {
        do {
            var hasMerged = false
            loop@ for (i in freshRanges.indices) {
                for (j in (i + 1) until freshRanges.size) {
                    if (freshRanges[i].isOverlapping(freshRanges[j])) {
                        val merged = merge(freshRanges[i], freshRanges[j])
                        freshRanges.removeAt(j)
                        freshRanges.removeAt(i)
                        freshRanges += merged
                        hasMerged = true
                        break@loop
                    }
                }
            }
        } while (hasMerged)

        println(freshRanges.sumOf { it.last - it.first + 1 })
    }

    private fun merge(r1: LongRange, r2: LongRange): LongRange {
        return min(r1.first, r2.first)..max(r1.last, r2.last)
    }

    private fun LongRange.isOverlapping(other: LongRange): Boolean {
        return !(last < other.first || first > other.last)
    }
}
