package aoc2016

import AocDay2
import Input

class Day18 : AocDay2 {

    private lateinit var firstRow: String

    override fun readInput(input: Input) {
        firstRow = input.asText()
    }

    override fun part1() {
        var row = firstRow
        var result = 0
        repeat(40) {
            result += row.count { it == '.' }
            row = nextRow(row)
        }
        println(result)
    }

    override fun part2() {
        var row = firstRow
        var result = 0
        repeat(400000) {
            result += row.count { it == '.' }
            row = nextRow(row)
        }
        println(result)
    }

    private fun nextRow(row: String): String = buildString {
        for (col in row.indices) {
            val left = row.getOrNull(col - 1) ?: '.'
            val center = row[col]
            val right = row.getOrNull(col + 1) ?: '.'
            append(
                when {
                    left == '^' && center == '^' && right == '.' -> '^'
                    left == '.' && center == '^' && right == '^' -> '^'
                    left == '^' && center == '.' && right == '.' -> '^'
                    left == '.' && center == '.' && right == '^' -> '^'
                    else -> '.'
                }
            )
        }
    }

}
