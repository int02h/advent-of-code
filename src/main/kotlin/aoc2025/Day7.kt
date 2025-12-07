package aoc2025

import AocDay2
import Input

class Day7 : AocDay2 {

    private lateinit var manifold: Array<CharArray>
    private val pathSet = mutableSetOf<String>()
    private val cache = mutableMapOf<String, Long>()

    override fun readInput(input: Input) {
        manifold = input.asLines().map { it.toCharArray() }.toTypedArray()
    }

    override fun part1() {
        var splitCount = 0
        val width = manifold[0].size
        for (row in 1 until manifold.size) {
            for (col in 0 until width) {
                val ch = manifold[row][col]
                when (ch) {
                    '^' -> {
                        if (manifold[row - 1][col] == '|') {
                            splitCount++
                            if (manifold[row][col - 1] == '.') {
                                manifold[row][col - 1] = '|'
                            }
                            manifold[row][col + 1] = '|'
                        }
                    }
                    '.' -> {
                        if (manifold[row - 1][col] == '|' || manifold[row - 1][col] == 'S') {
                            manifold[row][col] = '|'
                        }
                    }
                }
            }
        }
        println(splitCount)
    }

    override fun part2() {
//        travel(1, manifold[0].indexOf('S'), "")
//        println(pathSet.size)

        println(travel2(1, manifold[0].indexOf('S'), ""))
    }

    private fun travel(row: Int, col: Int, path: String) {
        if (row == manifold.size) {
            pathSet += path
            return
        }
        when (manifold[row][col]) {
            '^' -> {
                travel(row, col + 1, path + "R")
                travel(row, col - 1, path + "L")
            }
            '.' -> travel(row + 1, col, path)
        }
    }

    private fun travel2(row: Int, col: Int, path: String): Long {
        if (row == manifold.size) {
            return 1L
        }
        if (cache.contains("r:$row;c:$col")) {
            return cache.getValue("r:$row;c:$col")
        }
        val count = when (manifold[row][col]) {
            '^' -> travel2(row, col + 1, path + "R") + travel2(row, col - 1, path + "L")
            '.' -> travel2(row + 1, col, path)
            else -> error("oops")
        }
        cache["r:$row;c:$col"] = count
        return count
    }

}