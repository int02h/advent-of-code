package aoc2017

import AocDay2
import Input
import kotlin.math.abs
import kotlin.math.sqrt
import util.RowColPosition as Pos

class Day3 : AocDay2 {

    private var value = -1

    override fun readInput(input: Input) {
        value = input.asText().trim().toInt()
    }

    override fun part1() {
        var s = sqrt(value.toFloat()).toInt()
        if (s % 2 == 0) {
            s--
        }

        val len = s + 1
        val distFromCorner = (value - s * s) % len
        val distToMid = abs(len / 2 - distFromCorner)
        println(len / 2 + distToMid)
    }

    override fun part2() {
        val grid = mutableMapOf<Pos, Int>()
        var row = 0
        var col = 0
        grid[Pos(row, col)] = 1
        col++
        grid[Pos(row, col)] = 1

        var len = 2
        while (true) {
            repeat(len - 1) {
                row--
                grid[Pos(row, col)] = sunAround(grid, row, col)
                if (grid.getValue(Pos(row, col)) > value) {
                    println(grid.getValue(Pos(row, col)))
                    return
                }
            }
            repeat(len) {
                col--
                grid[Pos(row, col)] = sunAround(grid, row, col)
                if (grid.getValue(Pos(row, col)) > value) {
                    println(grid.getValue(Pos(row, col)))
                    return
                }
            }
            repeat(len) {
                row++
                grid[Pos(row, col)] = sunAround(grid, row, col)
                if (grid.getValue(Pos(row, col)) > value) {
                    println(grid.getValue(Pos(row, col)))
                    return
                }
            }
            repeat(len + 1) {
                col++
                grid[Pos(row, col)] = sunAround(grid, row, col)
                if (grid.getValue(Pos(row, col)) > value) {
                    println(grid.getValue(Pos(row, col)))
                    return
                }
            }
            len += 2
        }
    }

    private fun sunAround(grid: Map<Pos, Int>, row: Int, col: Int): Int {
        val p = Pos(row, col)
        return grid.getOrDefault(p.up, 0) +
                grid.getOrDefault(p.upRight, 0) +
                grid.getOrDefault(p.upLeft, 0) +
                grid.getOrDefault(p.right, 0) +
                grid.getOrDefault(p.left, 0) +
                grid.getOrDefault(p.down, 0) +
                grid.getOrDefault(p.downRight, 0) +
                grid.getOrDefault(p.downLeft, 0)
    }

}