package aoc2023

import AocDay
import Input

object Day14 : AocDay {

    override fun part1(input: Input) {
        val platform = input.asLines().map { it.toCharArray() }
        tiltNorth(platform)
        println(calcLoad(platform))
    }

    override fun part2(input: Input) {
        val platform = input.asLines().map { it.toCharArray() }
        val loads = mutableListOf<Int>()
        repeat(1000) {
            tiltNorth(platform)
            tiltWest(platform)
            tiltSouth(platform)
            tiltEast(platform)
            loads.add(0, calcLoad(platform))
        }

        var len = 2
        val cycle = mutableListOf<Int>()
        while (true) {
            val copy = loads.toMutableList()
            val part1 = copy.take(len)
            val part2 = copy.drop(len).take(len)
            if (part1 == part2) {
                cycle.addAll(part1)
                break
            }
            len++
        }

        var nonRepeatable = loads.toList()
        while (nonRepeatable.take(cycle.size) == cycle) {
            nonRepeatable = nonRepeatable.drop(cycle.size)
        }

        val totalCount = 1_000_000_000
        val fullCycles = (totalCount - nonRepeatable.size) / cycle.size
        // maybe cyclePart is always zero
        val cyclePart = totalCount - (fullCycles * cycle.size + nonRepeatable.size)
        println(cycle[cyclePart])
    }

    private fun calcLoad(platform: List<CharArray>): Int {
        var load = platform.size
        return platform.sumOf { row -> (load--) * row.count { cell -> cell == 'O' } }
    }

    private fun tiltNorth(platform: List<CharArray>) {
        for (col in platform[0].indices) {
            val rockRows = platform.withIndex().filter { it.value[col] == 'O' }.map { it.index }
            rockRows.forEach { rr ->
                var row = rr
                platform[row][col] = '.'
                while (row >= 0 && platform[row][col] == '.') row--
                platform[row + 1][col] = 'O'
            }
        }
    }

    private fun tiltSouth(platform: List<CharArray>) {
        for (col in platform[0].indices) {
            val rockRows = platform.withIndex().filter { it.value[col] == 'O' }.map { it.index }.reversed()
            rockRows.forEach { rr ->
                var row = rr
                platform[row][col] = '.'
                while (row < platform.size && platform[row][col] == '.') row++
                platform[row - 1][col] = 'O'
            }
        }
    }

    private fun tiltWest(platform: List<CharArray>) {
        for (row in platform.indices) {
            val rockCols = platform[row].withIndex().filter { it.value == 'O' }.map { it.index }
            rockCols.forEach { rc ->
                var col = rc
                platform[row][col] = '.'
                while (col >= 0 && platform[row][col] == '.') col--
                platform[row][col + 1] = 'O'
            }
        }
    }

    private fun tiltEast(platform: List<CharArray>) {
        for (row in platform.indices) {
            val rockCols = platform[row].withIndex().filter { it.value == 'O' }.map { it.index }.reversed()
            rockCols.forEach { rc ->
                var col = rc
                platform[row][col] = '.'
                while (col < platform[row].size && platform[row][col] == '.') col++
                platform[row][col - 1] = 'O'
            }
        }
    }
}