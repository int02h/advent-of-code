package aoc2025

import AocDay2
import Input
import util.RowColPosition

class Day4 : AocDay2 {

    private lateinit var grid: List<CharArray>

    override fun readInput(input: Input) {
        grid = input.asLines().map { it.toCharArray() }
    }

    override fun part1() {
        var count = 0
        for (row in 0 until grid.size) {
            for (col in 0 until grid[row].size) {
                if (grid[row][col] != '@') continue
                if (countPaperRolls(row, col) < 4) count++
            }
        }
        println(count)
    }

    override fun part2() {
        var total = 0
        do {
            val count = removeAllPaperRolls()
            total += count
        } while (count > 0)
        println(total)
    }

    private fun countPaperRolls(row: Int, col: Int): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                if (grid.getOrNull(row + dr)?.getOrNull(col + dc) == '@') count++
            }
        }
        return count
    }

    private fun removeAllPaperRolls(): Int {
        val toRemove = mutableListOf<RowColPosition>()
        for (row in 0 until grid.size) {
            for (col in 0 until grid[row].size) {
                if (grid[row][col] != '@') continue
                if (countPaperRolls(row, col) < 4) {
                    toRemove += RowColPosition(row, col)
                }
            }
        }
        toRemove.forEach { (row, col) -> grid[row][col] = '.' }
        return toRemove.size
    }
}