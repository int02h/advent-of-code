package aoc2019

import AocDay
import Input
import kotlin.math.atan

object Day10 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines()
        val (best, bestRow, bestCol) = findBest(map)
        println("${bestCol}x${bestRow} = $best")
    }

    override fun part2(input: Input) {
        val (_, bestRow, bestCol) = findBest(input.asLines())
        val map = input.asLines().map { it.toCharArray() }

        var angle = -1.0
        var vaporized = 0
        while (true) {
            val visible = getVisibleFromPosition(map, bestRow, bestCol)
            var bestAngle = Double.MAX_VALUE
            var toVaporize: Position? = null
            visible.forEach { v ->
                val vAngle = v.angleFrom(bestRow, bestCol)
                if (vAngle > angle && vAngle < bestAngle) {
                    bestAngle = vAngle
                    toVaporize = v
                }
            }
            if (toVaporize != null) {
                vaporized++
                map[toVaporize!!.row][toVaporize!!.col] = '.'
                angle = bestAngle
                if (vaporized == 200) {
                    println(toVaporize!!.col*100 + toVaporize!!.row)
                    break
                }
            }
        }
    }

    private fun findBest(map: List<String>): Triple<Int, Int, Int> {
        var best = 0
        var bestRow = -1
        var bestCol = -1
        for (row in map.indices) {
            for (col in map[0].indices) {
                if (map[row][col] == '#') {
                    val visible = countVisibleFromPosition(map, row, col)
                    if (visible > best) {
                        best = visible
                        bestRow = row
                        bestCol = col
                    }
                }
            }
        }
        return Triple(best, bestRow, bestCol)
    }

    private fun countVisibleFromPosition(map: List<String>, row: Int, col: Int): Int {
        val maxDRow = map.size - 1
        val maxDCol = map[0].length - 1
        val mapCopy = map.map { it.toCharArray() }
        for (dRow in 0..maxDRow) {
            for (dCol in 0..maxDCol) {
                if (dRow == 0 && dCol == 0) continue
                visitLine(mapCopy, row, col, dRow, dCol)
                visitLine(mapCopy, row, col, -dRow, dCol)
                visitLine(mapCopy, row, col, dRow, -dCol)
                visitLine(mapCopy, row, col, -dRow, -dCol)
            }
        }
        return mapCopy.sumOf { line -> line.count { it == 'v' } }
    }

    private fun visitLine(map: List<CharArray>, row: Int, col: Int, dRow: Int, dCol: Int) {
        var r = row + dRow
        var c = col + dCol
        var visible = 'v'
        while (r >= 0 && r < map.size && c >= 0 && c < map[0].size) {
            if (map[r][c] == '#') {
                map[r][c] = visible
                visible = 'i'
            }
            r += dRow
            c += dCol
        }
    }

    private fun getVisibleFromPosition(map: List<CharArray>, row: Int, col: Int): List<Position> {
        val maxDRow = map.size - 1
        val maxDCol = map[0].size - 1
        for (dRow in 0..maxDRow) {
            for (dCol in 0..maxDCol) {
                if (dRow == 0 && dCol == 0) continue
                visitLine(map, row, col, dRow, dCol)
                visitLine(map, row, col, -dRow, dCol)
                visitLine(map, row, col, dRow, -dCol)
                visitLine(map, row, col, -dRow, -dCol)
            }
        }
        val result = mutableListOf<Position>()
        map.forEachIndexed { r, line ->
            line.forEachIndexed { c, cell ->
                if (cell == 'v') {
                    result += Position(r, c)
                    map[r][c] = '#'
                } else if (cell == 'i') {
                    map[r][c] = '#'
                }
            }
        }
        return result
    }

    private data class Position(val row: Int, val col: Int) {
        fun angleFrom(r: Int, c: Int): Double = when {
            (col - c) > 0 -> 90 - 180 * atan(1.0 * (r - row) / (col - c)) / Math.PI
            (col - c) < 0 -> 270 + 180 * atan(1.0 * (r - row) / -(col - c)) / Math.PI
            else -> if ((r - row) >= 0) 0.0 else 180.0
        }
    }
}