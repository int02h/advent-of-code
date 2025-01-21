package aoc2016

import AocDay2
import Input

class Day2 : AocDay2 {

    private lateinit var lines: List<String>

    override fun readInput(input: Input) {
        lines = input.asLines()
    }

    override fun part1() {
        val keypad = arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(4, 5, 6),
            intArrayOf(7, 8, 9)
        )

        var row = 1
        var col = 1
        lines.forEach { line ->
            line.forEach { cmd ->
                when (cmd) {
                    'U' -> if (row > 0) row--
                    'D' -> if (row < 2) row++
                    'L' -> if (col > 0) col--
                    'R' -> if (col < 2) col++
                }
            }
            print(keypad[row][col])
        }
        println()
    }

    override fun part2() {
        val keypad = arrayOf(
            charArrayOf(' ', ' ', '1', ' ', ' '),
            charArrayOf(' ', '2', '3', '4', ' '),
            charArrayOf('5', '6', '7', '8', '9'),
            charArrayOf(' ', 'A', 'B', 'C', ' '),
            charArrayOf(' ', ' ', 'D', ' ', ' '),
        )

        var row = 2
        var col = 0
        lines.forEach { line ->
            line.forEach { cmd ->
                val (nr, nc) = when (cmd) {
                    'U' -> row - 1 to col
                    'D' -> row + 1 to col
                    'L' -> row to col - 1
                    'R' -> row to col + 1
                    else -> error(cmd)
                }
                if ((keypad.getOrNull(nr)?.getOrNull(nc) ?: ' ') != ' ') {
                    row = nr
                    col = nc
                }
            }
            print(keypad[row][col])
        }
        println()
    }
}