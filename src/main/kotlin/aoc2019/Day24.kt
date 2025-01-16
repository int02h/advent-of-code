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
        val initialPlanet = mutableMapOf<Position, Char>()
        for ((row, line) in initialState.withIndex()) {
            for ((col, ch) in line.withIndex()) {
                if (row == 2 && col == 2) continue
                initialPlanet[p(row, col, 0)] = ch
                initialPlanet[p(row, col, -1)] = '.'
                initialPlanet[p(row, col, 1)] = '.'
            }
        }

        var planet: Map<Position, Char> = initialPlanet
        repeat(200)
        {
            planet = nextState2(planet)
        }
        println(planet.count { it.value == '#' })
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

    private fun nextState2(planet: Map<Position, Char>): Map<Position, Char> {
        val result = mutableMapOf<Position, Char>()
        planet.forEach { (p, ch) ->
            val adjacent = getAdjacent2(p)
            val liveCount = adjacent.count { planet[it] == '#' }
            result[p] = if (ch == '#') {
                if (liveCount != 1) '.' else '#'
            } else {
                if (liveCount == 1 || liveCount == 2) '#' else '.'
            }

            if (result[p] == '#') {
                adjacent.forEach { result[it] = result[it] ?: '.' }
            }
        }
        return result
    }

    private fun getAdjacent2(pos: Position): List<Position> {
        val res = mutableListOf<Position>()
        val up = pos.copy(row = pos.row - 1)
        when {
            up.row == -1 -> res += p(1, 2, pos.level - 1)
            up.row == 2 && up.col == 2 -> res += (0..4).map { col -> p(4, col, pos.level + 1) }
            else -> res += up
        }

        val down = pos.copy(row = pos.row + 1)
        when {
            down.row == 5 -> res += p(3, 2, pos.level - 1)
            down.row == 2 && down.col == 2 -> res += (0..4).map { col -> p(0, col, pos.level + 1) }
            else -> res += down
        }

        val left = pos.copy(col = pos.col - 1)
        when {
            left.col == -1 -> res += p(2, 1, pos.level - 1)
            left.col == 2 && left.row == 2 -> res += (0..4).map { row -> p(row, 4, pos.level + 1) }
            else -> res += left
        }

        val right = pos.copy(col = pos.col + 1)
        when {
            right.col == 5 -> res += p(2, 3, pos.level - 1)
            right.col == 2 && right.row == 2 -> res += (0..4).map { row -> p(row, 0, pos.level + 1) }
            else -> res += right
        }
        return res
    }

    private fun p(row: Int, col: Int, level: Int) = Position(row, col, level)

    private data class Position(val row: Int, val col: Int, val level: Int)
}