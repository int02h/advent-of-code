package aoc2019

import AocDay2
import Input

class Day24 : AocDay2 {

    private lateinit var initialState: List<String>

    override fun readInput(input: Input) {
        initialState = input.asLines()
    }

    override fun part1() {
        val visited = mutableSetOf<List<String>>()
        var state = initialState
        do {
            state = nextState(state)
        } while (visited.add(state))

        var power2 = 1L
        var result = 0L
        state.forEach { line ->
            line.forEach { cell ->
                if (cell == '#') result += power2
                power2 *= 2
            }
        }
        println(result)
    }

    override fun part2() {
    }

    private fun nextState(state: List<String>): List<String> {
        val result = mutableListOf<String>()
        state.forEachIndexed { row, line ->
            var newLine = ""
            line.forEachIndexed { col, ch ->
                var adjacent = 0
                if (state.getOrNull(row - 1)?.getOrNull(col) == '#') adjacent++
                if (state.getOrNull(row + 1)?.getOrNull(col) == '#') adjacent++
                if (state.getOrNull(row)?.getOrNull(col - 1) == '#') adjacent++
                if (state.getOrNull(row)?.getOrNull(col + 1) == '#') adjacent++
                newLine += if (ch == '#') {
                    if (adjacent != 1) '.' else '#'
                } else {
                    if (adjacent == 1 || adjacent == 2) '#' else '.'
                }
            }
            result += newLine
        }
        return result
    }
}