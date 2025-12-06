package aoc2025

import AocDay2
import Input

class Day6 : AocDay2 {

    private lateinit var lines: List<String>

    override fun readInput(input: Input) {
        lines = input.asLines()
    }

    override fun part1() {
        val values = lines.dropLast(1).map { line ->
            line.split(" ")
                .filter { v -> v.isNotEmpty() }
                .map { it.toLong() }
        }
        val ops = lines.last().split(" ").filter { v -> v.isNotEmpty() }

        var total = 0L
        ops.forEachIndexed { index, op ->
            val col = values.map { it[index] }
            total += if (op == "+") {
                col.fold(0L) { acc, v -> acc + v }
            } else {
                col.fold(1L) { acc, v -> acc * v }
            }
        }
        println(total)
    }

    override fun part2() {
        val ops = mutableListOf<Char>()
        val columns = mutableListOf<Int>()
        lines.last().forEachIndexed { index, ch ->
            if (ch != ' ') {
                ops += ch
                columns += index
            }
        }

        var total = 0L
        ops.forEachIndexed { index, op ->
            val col = getColumn(columns[index])
            total += if (op == '+') {
                col.fold(0L) { acc, v -> acc + v }
            } else {
                col.fold(1L) { acc, v -> acc * v }
            }
        }
        println(total)
    }

    private fun getColumn(startPos: Int): List<Long> {
        val res = mutableListOf<Long>()
        var col = startPos
        while (true) {
            var num = ""
            for (row in 0 until lines.lastIndex) {
                val ch = lines[row].getOrNull(col) ?: ' '
                if (ch != ' ') num += ch
            }
            if (num == "") {
                break
            }
            res += num.toLong()
            col++
        }
        return res
    }

}
