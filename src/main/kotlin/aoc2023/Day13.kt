package aoc2023

import AocDay
import Input

object Day13 : AocDay {
    override fun part1(input: Input) {
        val patterns = parse(input)
        println(
            patterns.sumOf { (it.findHorizontal() ?: 0) * 100 + (it.findVertical() ?: 0) }
        )
    }

    override fun part2(input: Input) {
        val patterns = parse(input)
        println(
            patterns.sumOf {
                val oldH = it.findHorizontal()
                val oldV = it.findVertical()
                var res = 0
                while (it.nextSmudge()) {
                    val newH = it.findHorizontal(oldH)
                    if (newH != null) {
                        res = newH * 100
                        break
                    }
                    val newV = it.findVertical(oldV)
                    if (newV != null) {
                        res = newV
                        break
                    }
                }
                res
            }
        )
    }

    private fun parse(input: Input): List<Pattern> {
        val patterns = input.asText().split("\n\n")
        return patterns.map { p ->
            Pattern(p.split("\n").map { it.toCharArray() })
        }
    }

    private class Pattern(val rows: List<CharArray>) {

        var smudgeIndex: Int = -1

        val columns = (0 until rows[0].size).map { col ->
            buildString {
                rows.forEach { append(it[col]) }
            }.toCharArray()
        }

        fun nextSmudge(): Boolean {
            if (smudgeIndex >= 0) {
                invertSmudge()
            }
            smudgeIndex++
            if (smudgeIndex >= rows.size * columns.size) {
                return false
            }
            invertSmudge()
            return true
        }

        fun findVertical(exceptLine: Int? = null): Int? = find(columns, exceptLine)

        fun findHorizontal(exceptLine: Int? = null): Int? = find(rows, exceptLine)

        private fun invertSmudge() {
            val row = smudgeIndex / columns.size
            val col = smudgeIndex % columns.size
            rows[row][col] = if (rows[row][col] == '#') '.' else '#'
            columns[col][row] = if (columns[col][row] == '#') '.' else '#'
        }

        companion object {
            private fun find(list: List<CharArray>, exceptLine: Int?): Int? {
                val reflectionLines = mutableListOf<Int>()
                for (i in 1..list.lastIndex) {
                    if (list[i - 1].contentEquals(list[i])) {
                        reflectionLines += i
                    }
                }
                exceptLine?.let { reflectionLines.remove(it) }
                if (reflectionLines.isEmpty()) {
                    return null
                }
                return reflectionLines.find { line ->
                    var r = line - 1
                    var l = line
                    var found = true
                    while (l >= 0 && r < list.size) {
                        if (!list[l].contentEquals(list[r])) {
                            found = false
                            break
                        }
                        r++
                        l--
                    }
                    found
                }
            }
        }
    }
}