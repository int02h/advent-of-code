package aoc2024

import AocDay
import Input

object Day4 : AocDay {

    override fun part1(input: Input) {
        val lines = input.asLines()
        var result = 0
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                result += search(lines, row, col)
            }
        }
        println(result)
    }

    override fun part2(input: Input) {
        val lines = input.asLines()
        var result = 0
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (lines[row][col] == 'A') {
                    val words = getXWords(lines, row, col)
                    if (words.all { w -> w == "MAS" || w == "SAM" }) {
                        result += 1
                    }
                }
            }
        }
        println(result)
    }

    private fun search(lines: List<String>, row: Int, col: Int): Int {
        return listOf(
            hasWord(lines, row, col, dRow = 0, dCol = 1),
            hasWord(lines, row, col, dRow = 0, dCol = -1),
            hasWord(lines, row, col, dRow = 1, dCol = 0),
            hasWord(lines, row, col, dRow = -1, dCol = 0),
            hasWord(lines, row, col, dRow = 1, dCol = 1),
            hasWord(lines, row, col, dRow = 1, dCol = -1),
            hasWord(lines, row, col, dRow = -1, dCol = -1),
            hasWord(lines, row, col, dRow = -1, dCol = 1),
        ).count { it }
    }

    private fun hasWord(lines: List<String>, startRow: Int, startCol: Int, dRow: Int, dCol: Int): Boolean {
        var row = startRow
        var col = startCol
        var word = ""
        repeat(4) {
            lines.getOrNull(row)?.getOrNull(col)?.let { ch -> word += ch }
            row += dRow
            col += dCol
        }
        return word == "XMAS"
    }

    private fun getXWords(lines: List<String>, row: Int, col: Int): List<String> {
        return listOf(
            buildString(
                lines.getOrNull(row + 1)?.getOrNull(col - 1),
                lines.getOrNull(row)?.getOrNull(col),
                lines.getOrNull(row - 1)?.getOrNull(col + 1),
            ),
            buildString(
                lines.getOrNull(row - 1)?.getOrNull(col - 1),
                lines.getOrNull(row)?.getOrNull(col),
                lines.getOrNull(row + 1)?.getOrNull(col + 1),
            )
        )
    }

    private fun buildString(vararg chars: Char?): String {
        return chars.filterNotNull().joinToString(separator = "")
    }

}