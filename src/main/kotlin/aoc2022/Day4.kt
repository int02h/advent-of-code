package aoc2022

import Input

object Day4 {

    fun part1(input: Input) {
        input.asLines()
            .map { it.split(",").map(::toRange) }
            .map { (range1, range2) -> range1.fullyContains(range2) || range2.fullyContains(range1) }
            .count{ it }
            .also(::println)
    }

    fun part2(input: Input) {
        input.asLines()
            .map { it.split(",").map(::toRange) }
            .map { (range1, range2) -> range1.overlaps(range2) }
            .count{ it }
            .also(::println)
    }

    private fun toRange(value: String): IntRange {
        val (start, end) = value.split("-").map { it.toInt() }
        return start..end
    }

    private fun IntRange.fullyContains(other: IntRange): Boolean {
        return this.first <= other.first && other.last <= this.last
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        return other.first <= this.last && other.last >= this.first
    }

}
