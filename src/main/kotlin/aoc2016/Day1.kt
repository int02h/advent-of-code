package aoc2016

import AocDay2
import Input
import util.RowColPosition
import kotlin.math.abs

class Day1 : AocDay2 {

    private lateinit var instructions: List<Instruction>

    override fun readInput(input: Input) {
        instructions = input.asText()
            .split(", ")
            .map { Instruction(it[0], it.substring(1).toInt()) }
    }

    override fun part1() {
        var row = 0
        var col = 0
        var direction = Direction.NORTH
        instructions.forEach {
            direction = when (direction) {
                Direction.NORTH -> if (it.turn == 'R') Direction.EAST else Direction.WEST
                Direction.SOUTH -> if (it.turn == 'R') Direction.WEST else Direction.EAST
                Direction.WEST -> if (it.turn == 'R') Direction.NORTH else Direction.SOUTH
                Direction.EAST -> if (it.turn == 'R') Direction.SOUTH else Direction.NORTH
            }
            row += direction.dRow * it.stepCount
            col += direction.dCol * it.stepCount
        }
        println(abs(row) + abs(col))
    }

    override fun part2() {
        var row = 0
        var col = 0
        var direction = Direction.NORTH
        val visited = mutableSetOf(RowColPosition(row, col))
        for (it in instructions) {
            direction = when (direction) {
                Direction.NORTH -> if (it.turn == 'R') Direction.EAST else Direction.WEST
                Direction.SOUTH -> if (it.turn == 'R') Direction.WEST else Direction.EAST
                Direction.WEST -> if (it.turn == 'R') Direction.NORTH else Direction.SOUTH
                Direction.EAST -> if (it.turn == 'R') Direction.SOUTH else Direction.NORTH
            }
            var count = it.stepCount
            while (count > 0) {
                row += direction.dRow
                col += direction.dCol
                count--
                if (!visited.add(RowColPosition(row, col))) {
                    println(abs(row) + abs(col))
                    return
                }
            }
        }
    }

    private data class Instruction(val turn: Char, val stepCount: Int)

    private enum class Direction(val dRow: Int = 0, val dCol: Int = 0) {
        NORTH(dRow = -1),
        SOUTH(dRow = 1),
        EAST(dCol = 1),
        WEST(dCol = -1)
    }
}