package aoc2023

import AocDay
import Input

object Day10 : AocDay {

    override fun part1(input: Input) {
        println(Walker(parse(input)).walkSketch())
    }

    override fun part2(input: Input) {
        val sketch = parse(input)
        val s = sketch.map { it.toMutableList() }

        Walker(sketch).apply {
            walkSketch()
            map.forEachIndexed { row, line ->
                line.forEachIndexed { col, cell ->
                    if (!cell) {
                        s[row][col] = '.'
                    }
                }
            }
        }

        val walker = Walker(s.map { String(it.toCharArray()) })
        walker.walkSketch()
        if (
            walker.right.any { it.row <= 0 || it.col <= 0 || it.row >= sketch.lastIndex || it.col >= sketch[0].lastIndex }
        ) {
            walker.left.forEach { p ->
                fill(s, p.row, p.col)
            }
        } else {
            walker.right.forEach { p ->
                fill(s, p.row, p.col)
            }
        }
        println(
            s.sumOf { line -> line.count { it == '*' } }
        )
    }

    private fun fill(sketch: List<MutableList<Char>>, row: Int, col: Int) {
        val stack = mutableListOf<Point>()
        stack.add(Point(row, col))
        while (stack.isNotEmpty()) {
            val p = stack.removeLast()
            sketch[p.row][p.col] = '*'
            if (sketch[p.row][p.col + 1] == '.') {
                stack.add(Point(p.row, p.col + 1))
            }
            if (sketch[p.row][p.col - 1] == '.') {
                stack.add(Point(p.row, p.col - 1))
            }
            if (sketch[p.row + 1][p.col] == '.') {
                stack.add(Point(p.row + 1, p.col))
            }
            if (sketch[p.row - 1][p.col] == '.') {
                stack.add(Point(p.row - 1, p.col))
            }
        }
    }

    private fun parse(input: Input): List<String> = input.asLines()

    private class Walker(val sketch: List<String>) {

        val left = mutableSetOf<Point>()
        val right = mutableSetOf<Point>()
        val map = Array(sketch.size) { BooleanArray(sketch.first().length) }

        fun walkSketch(): Int {
            var row = 0
            var col = 0
            var ch: Char = sketch[row][col]

            while (ch != 'S') {
                col++
                if (col == sketch[row].length) {
                    row++
                    col = 0
                }
                ch = sketch[row][col]
            }

            val up = sketch.getOrNull(row - 1)?.getOrNull(col)
            if (up == '|' || up == '7' || up == 'F') {
                return walk(sketch, row, col, -1, 0)
            }
            val down = sketch.getOrNull(row + 1)?.getOrNull(col)
            if (down == '|' || down == 'L' || down == 'J') {
                return walk(sketch, row, col, 1, 0)
            }
            val left = sketch[row].getOrNull(col - 1)
            if (left == '-' || left == 'L' || left == 'F') {
                return walk(sketch, row, col, 0, -1)
            }
            val right = sketch[row].getOrNull(col + 1)
            if (right == '-' || right == 'J' || right == '7') {
                return walk(sketch, row, col, 0, 1)
            }
            error("Impossible")
        }

        private fun walk(
            sketch: List<String>,
            startRow: Int,
            startCol: Int,
            startRowDiff: Int,
            startColDiff: Int
        ): Int {
            var length = 0
            var rowDiff = startRowDiff
            var colDiff = startColDiff
            var row = startRow + rowDiff
            var col = startCol + colDiff
            var ch: Char = sketch[row][col]
            map[row][col] = true

            while (ch != 'S') {
                when (ch) {
                    'L' -> {
                        if (colDiff != 0) {
                            rowDiff = colDiff
                            colDiff = 0
                            if (sketch.isDotOrNull(row + 1, col)) {
                                left += Point(row + 1, col)
                            }
                            if (sketch.isDotOrNull(row, col - 1)) {
                                left += Point(row, col - 1)
                            }
                        } else {
                            colDiff = rowDiff
                            rowDiff = 0
                            if (sketch.isDotOrNull(row, col - 1)) {
                                right += Point(row, col - 1)
                            }
                            if (sketch.isDotOrNull(row + 1, col)) {
                                right += Point(row + 1, col)
                            }
                        }
                    }
                    '7' -> {
                        if (colDiff != 0) {
                            rowDiff = colDiff
                            colDiff = 0
                            if (sketch.isDotOrNull(row + 1, col)) {
                                left += Point(row + 1, col)
                            }
                            if (sketch.isDotOrNull(row, col + 1)) {
                                left += Point(row, col + 1)
                            }
                        } else {
                            colDiff = rowDiff
                            rowDiff = 0
                            if (sketch.isDotOrNull(row, col + 1)) {
                                right += Point(row, col + 1)
                            }
                            if (sketch.isDotOrNull(row - 1, col)) {
                                right += Point(row - 1, col)
                            }
                        }
                    }
                    'J' -> {
                        if (colDiff != 0) {
                            rowDiff = -colDiff
                            colDiff = 0
                            if (sketch.isDotOrNull(row + 1, col)) {
                                right += Point(row + 1, col)
                            }
                            if (sketch.isDotOrNull(row, col + 1)) {
                                right += Point(row, col + 1)
                            }
                        } else {
                            colDiff = -rowDiff
                            rowDiff = 0
                            if (sketch.isDotOrNull(row, col + 1)) {
                                left += Point(row, col + 1)
                            }
                            if (sketch.isDotOrNull(row + 1, col)) {
                                left += Point(row + 1, col)
                            }
                        }
                    }
                    'F' -> {
                        if (colDiff != 0) {
                            rowDiff = -colDiff
                            colDiff = 0
                            if (sketch.isDotOrNull(row - 1, col)) {
                                right += Point(row - 1, col)
                            }
                            if (sketch.isDotOrNull(row, col - 1)) {
                                right += Point(row, col - 1)
                            }
                        } else {
                            colDiff = -rowDiff
                            rowDiff = 0
                            if (sketch.isDotOrNull(row, col - 1)) {
                                left += Point(row, col - 1)
                            }
                            if (sketch.isDotOrNull(row - 1, col)) {
                                left += Point(row - 1, col)
                            }
                        }
                    }
                    '|' -> {
                        if (rowDiff > 0) {
                            if (sketch.isDotOrNull(row, col - 1)) {
                                right += Point(row, col - 1)
                            }
                            if (sketch.isDotOrNull(row, col + 1)) {
                                left += Point(row, col + 1)
                            }
                        } else {
                            if (sketch.isDotOrNull(row, col + 1)) {
                                right += Point(row, col + 1)
                            }
                            if (sketch.isDotOrNull(row, col - 1)) {
                                left += Point(row, col - 1)
                            }
                        }
                    }
                    '-' -> {
                        if (colDiff > 0) {
                            if (sketch.isDotOrNull(row + 1, col)) {
                                right += Point(row + 1, col)
                            }
                            if (sketch.isDotOrNull(row - 1, col)) {
                                left += Point(row - 1, col)
                            }
                        } else {
                            if (sketch.isDotOrNull(row - 1, col)) {
                                right += Point(row - 1, col)
                            }
                            if (sketch.isDotOrNull(row + 1, col)) {
                                left += Point(row + 1, col)
                            }
                        }
                    }
                }
                row += rowDiff
                col += colDiff
                ch = sketch[row][col]
                map[row][col] = true
                length++
            }
            return length / 2 + 1
        }
    }

    private fun List<String>.isDotOrNull(row: Int, col: Int): Boolean {
        val cell = getOrNull(row)?.getOrNull(col)
        return cell == null || cell == '.'
    }

    private data class Point(val row: Int, val col: Int)

}