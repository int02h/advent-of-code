package aoc2024

import AocDay
import Input

object Day8 : AocDay {

    override fun part1(input: Input) {
        val antennas = mutableMapOf<Char, MutableList<Antenna>>()
        val (rowCount, colCount) = readInput(input, antennas)
        val antinodes = mutableSetOf<Antinode>()

        fun addAntinodes(a1: Antenna, a2: Antenna) {
            val dr = a1.row - a2.row
            val dc = a1.col - a2.col
            val row = a1.row + dr
            val col = a1.col + dc
            if (row in 0 until rowCount && col in 0 until colCount) {
                antinodes += Antinode(row, col)
            }
        }

        antennas.values.forEach { list ->
            for (i in list.indices) {
                for (j in (i + 1)..list.lastIndex) {
                    addAntinodes(list[i], list[j])
                    addAntinodes(list[j], list[i])
                }
            }
        }

        println(antinodes.size)
    }

    override fun part2(input: Input) {
        val antennas = mutableMapOf<Char, MutableList<Antenna>>()
        val (rowCount, colCount) = readInput(input, antennas)
        val antinodes = mutableSetOf<Antinode>()

        fun addAntinodes(a1: Antenna, a2: Antenna) {
            val dr = a1.row - a2.row
            val dc = a1.col - a2.col
            var row = a1.row
            var col = a1.col
            while (row in 0 until rowCount && col in 0 until colCount) {
                antinodes += Antinode(row, col)
                row += dr
                col += dc
            }
        }

        antennas.values.forEach { list ->
            for (i in list.indices) {
                for (j in (i + 1)..list.lastIndex) {
                    addAntinodes(list[i], list[j])
                    addAntinodes(list[j], list[i])
                }
            }
        }

        println(antinodes.size)
    }

    private fun readInput(input: Input, result: MutableMap<Char, MutableList<Antenna>>): Pair<Int, Int> {
        val lines = input.asLines()
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                if (ch != '.') {
                    result.getOrPut(ch) { mutableListOf() } += Antenna(ch, row, col)
                }
            }
        }
        return lines.size to lines[0].length
    }

    data class Antenna(
        val frequency: Char,
        val row: Int,
        val col: Int
    )

    data class Antinode(val row: Int, val col: Int)
}