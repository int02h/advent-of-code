package aoc2025

import AocDay2
import Input

class Day2 : AocDay2 {
    private lateinit var ranges: List<LongRange>

    override fun readInput(input: Input) {
        ranges = input.asText().split(",")
            .map { range ->
                val (start, end) = range.split("-").map { it.toLong() }
                start..end
            }
    }

    override fun part1() {
        var sum = 0L
        ranges.forEach { r ->
            for (id in r) {
                if (isInvalid(id)) sum += id
            }
        }
        println(sum)
    }

    override fun part2() {
        var sum = 0L
        ranges.forEach { r ->
            for (id in r) {
                if (isInvalid2(id)) sum += id
            }
        }
        println(sum)
    }

    private fun isInvalid(id: Long): Boolean {
        val str = id.toString()
        if (str.length % 2 == 1) {
            return false
        }
        val part1 = str.take(str.length / 2)
        val part2 = str.drop(str.length / 2)
        return part1 == part2
    }

    private fun isInvalid2(id: Long): Boolean {
        val str = id.toString()
        for (i in 1..str.length / 2) {
            val part = str.take(i)
            if (str.replace(part, "").isEmpty()) {
                return true
            }
        }
        return false
    }
}