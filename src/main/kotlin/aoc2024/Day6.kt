package aoc2024

import AocDay
import Input

object Day6 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines()
        val startRow = map.indexOfFirst { row -> row.contains('^') }
        val startCol = map[startRow].indexOf('^')
        val guard = Guard(map, Position(startRow, startCol))
        guard.run()
        println(guard.visited.size)
    }

    override fun part2(input: Input) {
        val map = input.asLines()
        val startRow = map.indexOfFirst { row -> row.contains('^') }
        val startCol = map[startRow].indexOf('^')
        var guard = Guard(map, Position(startRow, startCol))

        guard.run()
        val visitedPositions = guard.visited.drop(1)

        var result = 0
        visitedPositions.forEach { vp ->
            val copy = map.toMutableList()
            val row = copy[vp.row].toCharArray()
            row[vp.col] = '#'
            copy[vp.row] = String(row)
            guard = Guard(copy, Position(startRow, startCol))
            guard.run()
            if (guard.isLoopFound) {
                result++
            }
        }
        println(result)
    }

    private class Guard(
        val map: List<String>,
        var position: Position
    ) {

        val visited = mutableSetOf<Position>()
        var direction: Direction = Direction.UP
        var moveCount = 0

        var isLoopFound: Boolean = false

        fun run() {
            @Suppress("ControlFlowWithEmptyBody")
            while (nextMove()) {
            }
        }

        fun nextMove(): Boolean {
            visited += position
            moveCount++
            if (moveCount > 3 * visited.size) {
                isLoopFound = true
                return false
            }
            val nextPosition = position.move(direction)
            when (getCell(nextPosition)) {
                null -> return false
                '#' -> direction = direction.turnRight()
                '.', '^' -> position = nextPosition
                else -> error("Illegal")
            }
            return true
        }

        private fun getCell(position: Position): Char? {
            return map.getOrNull(position.row)?.getOrNull(position.col)
        }
    }

    private data class Position(val row: Int, val col: Int) {
        fun move(direction: Direction): Position = when (direction) {
            Direction.UP -> Position(row - 1, col)
            Direction.RIGHT -> Position(row, col + 1)
            Direction.DOWN -> Position(row + 1, col)
            Direction.LEFT -> Position(row, col - 1)
        }
    }

    private enum class Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        fun turnRight(): Direction = when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}