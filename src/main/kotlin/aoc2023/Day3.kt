package aoc2023

import AocDay
import Input

object Day3 : AocDay {

    override fun part1(input: Input) {
        val lines = input.asLines()
        val number = StringBuilder()
        var sum = 0
        for (row in lines.indices) {
            val line = lines[row]
            var isAdjacent = false
            for (col in line.indices) {
                if (line[col].isDigit()) {
                    isAdjacent = isAdjacent || hasSymbolsAround(lines, row, col)
                    number.append(line[col])
                } else {
                    if (isAdjacent) {
                        sum += number.toString().toInt()
                    }
                    isAdjacent = false
                    number.clear()
                }
            }

            if (isAdjacent) {
                sum += number.toString().toInt()
            }
            number.clear()
        }
        println(sum)
    }

    override fun part2(input: Input) {
        val lines = input.asLines()
        val number = StringBuilder()
        val asteriskMap = mutableMapOf<Position, MutableList<Long>>()

        for (row in lines.indices) {
            val line = lines[row]
            val adjacentAsteriskPositions = mutableSetOf<Position>()
            for (col in line.indices) {
                if (line[col].isDigit()) {
                    hasAdjacentAsterisk(lines, row, col)?.let(adjacentAsteriskPositions::add)
                    number.append(line[col])
                } else {
                    adjacentAsteriskPositions.forEach { p ->
                        asteriskMap.getOrPut(p) { mutableListOf() }.add(number.toString().toLong())
                    }
                    adjacentAsteriskPositions.clear()
                    number.clear()
                }
            }

            adjacentAsteriskPositions.forEach { p ->
                asteriskMap.getOrPut(p) { mutableListOf() }.add(number.toString().toLong())
            }
            number.clear()
        }

        println(
            asteriskMap.values.sumOf { if (it.size == 2) it[0] * it[1] else 0L }
        )
    }

    private fun hasSymbolsAround(lines: List<String>, row: Int, col: Int): Boolean {
        for (r in (row - 1)..(row + 1)) {
            for (c in (col - 1)..(col + 1)) {
                val ch = lines.getOrNull(r)?.getOrNull(c)
                if (ch != null && !ch.isDigit() && ch != '.') {
                    return true
                }
            }
        }
        return false
    }

    private fun hasAdjacentAsterisk(lines: List<String>, row: Int, col: Int): Position? {
        for (r in (row - 1)..(row + 1)) {
            for (c in (col - 1)..(col + 1)) {
                if (lines.getOrNull(r)?.getOrNull(c) == '*') {
                    return Position(r, c)
                }
            }
        }
        return null
    }

    private data class Position(val row: Int, val col: Int)

}