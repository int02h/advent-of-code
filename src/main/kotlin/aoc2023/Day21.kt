package aoc2023

import AocDay
import Input

object Day21 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines()
        val startPosition = getStartPosition(map)
        val gardenMap = GardenMap { pos ->
            val cell = map.getOrNull(pos.row)?.getOrNull(pos.col)
            cell == '.' || cell == 'S'
        }
        walk(gardenMap, startPosition, targetStepCount = 64)
    }

    override fun part2(input: Input) {
        val map = input.asLines()
        val startPosition = getStartPosition(map)
        val gardenMap = GardenMap { pos ->
            val row = wrapValue(pos.row, map.size)
            val col = wrapValue(pos.col, map[0].length)
            val cell = map[row][col]
            cell == '.' || cell == 'S'
        }
        // walk(gardenMap, startPosition, targetStepCount = 65) = 3751
        // walk(gardenMap, startPosition, targetStepCount = 196) = 33531
        // walk(gardenMap, startPosition, targetStepCount = 327) = 92991
        // equation: 14840*x^2 + 14940*x + 3751

        val steps = 26501365
        val x = (26501365 - 65) / 131L
        println(14840 * x * x + 14940 * x + 3751)
    }

    private fun wrapValue(value: Int, size: Int): Int {
        var result = value
        while (result < 0) result += size
        return result % size
    }

    private fun getStartPosition(map: List<String>): Position {
        var startRow = -1
        var startCol = -1
        for ((row, line) in map.withIndex()) {
            startCol = line.indexOf('S')
            if (startCol != -1) {
                startRow = row
                break
            }
        }
        return Position(startRow, startCol)
    }

    private fun walk(map: GardenMap, startPos: Position, targetStepCount: Int) {
        data class QueueEntry(val pos: Position, val steps: Int)

        val queue = mutableListOf<QueueEntry>()
        queue.add(QueueEntry(startPos, 0))

        val visited = mutableSetOf<QueueEntry>()
        val result = mutableSetOf<Position>()
        while (queue.isNotEmpty()) {
            val entry = queue.removeFirst()
            if (!visited.add(entry)) {
                continue
            }
            val pos = entry.pos
            if (entry.steps == targetStepCount) {
                result += pos
                continue
            }
            listOf(pos.up, pos.down, pos.left, pos.right).forEach { next ->
                val nextEntry = QueueEntry(next, entry.steps + 1)
                if (map.isPlot(next) && !visited.contains(nextEntry)) {
                    queue.add(nextEntry)
                }
            }
        }
        println(result.size)
    }

    private data class Position(val row: Int, val col: Int) {
        val up: Position get() = copy(row = row - 1)
        val down: Position get() = copy(row = row + 1)
        val left: Position get() = copy(col = col - 1)
        val right: Position get() = copy(col = col + 1)
    }

    private fun interface GardenMap {
        fun isPlot(pos: Position): Boolean
    }
}