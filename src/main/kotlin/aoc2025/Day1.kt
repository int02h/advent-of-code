package aoc2025

import AocDay2
import Input
import kotlin.math.abs
import kotlin.math.sign

class Day1 : AocDay2 {

    private val rotations = mutableListOf<Int>()

    override fun readInput(input: Input) {
        rotations += input.asLines()
            .map { line ->
                val value = line.substring(1).toInt()
                if (line[0] == 'L') -value else value
            }
    }

    override fun part1() {
        var pos = 50
        var count = 0
        rotations.forEach { r ->
            pos = (pos + r) % 100
            while (pos < 0) pos += 100
            if (pos == 0) count++
        }
        println(count)
    }

    override fun part2() {
        var pos = 50
        var count = 0
        rotations.forEach { r ->
            val sign = r.sign
            repeat(abs(r)) {
                pos += sign
                if (pos == 100) pos = 0
                if (pos == -1) pos = 99
                if (pos == 0) count++
            }
        }
        println(count)
    }
}