package aoc2015

import Input

object Day3 {

    fun part1(input: Input) {
        val visited = mutableSetOf<Position>()
        var position = Position(0, 0)
        visited += position

        input.asText().forEach { d ->
            when (d) {
                '<' -> position = position.copy(x = position.x - 1)
                '>' -> position = position.copy(x = position.x + 1)
                '^' -> position = position.copy(y = position.y - 1)
                'v' -> position = position.copy(y = position.y + 1)
            }
            visited += position
        }
        println(visited.size)
    }

    fun part2(input: Input) {
        val visited = mutableSetOf<Position>()
        val positions = arrayOf(Position(0, 0), Position(0, 0))
        var positionIndex = 0
        visited += positions[positionIndex]

        input.asText().forEach { d ->
            when (d) {
                '<' -> positions[positionIndex] = positions[positionIndex].copy(x = positions[positionIndex].x - 1)
                '>' -> positions[positionIndex] = positions[positionIndex].copy(x = positions[positionIndex].x + 1)
                '^' -> positions[positionIndex] = positions[positionIndex].copy(y = positions[positionIndex].y - 1)
                'v' -> positions[positionIndex] = positions[positionIndex].copy(y = positions[positionIndex].y + 1)
            }
            visited += positions[positionIndex]
            positionIndex = 1 - positionIndex
        }
        println(visited.size)
    }

    private data class Position(val x: Int, val y: Int)

}