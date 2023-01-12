package aoc2015

import AocDay
import Input

object Day18 : AocDay {

    private val neighborsIndices = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )

    override fun part1(input: Input) {
        var grid = readGrid(input)
        repeat(100) {
            grid = changeState(grid)
        }
        println(grid.sumOf { row -> row.count { it } })
    }

    override fun part2(input: Input) {
        var grid = readGrid(input)

        fun turnOnCorners() {
            grid[0][0] = true
            grid[0][grid[0].lastIndex] = true
            grid[grid.lastIndex][0] = true
            grid[grid.lastIndex][grid[0].lastIndex] = true
        }

        repeat(100) {
            turnOnCorners()
            grid = changeState(grid)
            turnOnCorners()
        }
        println(grid.sumOf { row -> row.count { it } })
    }

    private fun changeState(grid: Array<BooleanArray>): Array<BooleanArray> {
        val width = grid.first().size
        val height = grid.size
        val result = Array(height) { BooleanArray(width) }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val onNeighbors = countOnNeighbors(grid, x, y)
                if (grid[y][x]) {
                    result[y][x] = (onNeighbors == 2 || onNeighbors == 3)
                } else {
                    result[y][x] = (onNeighbors == 3)
                }
            }
        }

        return result
    }

    private fun countOnNeighbors(grid: Array<BooleanArray>, x: Int, y: Int): Int =
        neighborsIndices.count { (dx, dy) ->
            grid.getOrNull(y + dy)?.getOrNull(x + dx) ?: false
        }

    private fun readGrid(input: Input): Array<BooleanArray> {
        val grid = mutableListOf<MutableList<Boolean>>()
        input.asLines().forEach { line ->
            val row = mutableListOf<Boolean>()
            line.forEach { ch -> row.add(ch == '#') }
            grid.add(row)
        }
        return grid.map { it.toBooleanArray() }.toTypedArray()
    }

}