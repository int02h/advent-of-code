package aoc2015

import Input
import kotlin.math.max

object Day6 {

    fun part1(input: Input) {
        val grid = Array(1000) { BooleanArray(1000) }
        readInstructions(input).forEach { instruction ->
            val xRange = instruction.fromX..instruction.toX
            val yRange = instruction.fromY..instruction.toY
            for (y in yRange) {
                for (x in xRange) {
                    when (instruction.type) {
                        InstructionType.TURN_ON -> grid[y][x] = true
                        InstructionType.TURN_OFF -> grid[y][x] = false
                        InstructionType.TOGGLE -> grid[y][x] = !grid[y][x]
                    }
                }
            }
        }
        println(grid.sumOf { row -> row.count { it } })
    }

    fun part2(input: Input) {
        val grid = Array(1000) { IntArray(1000) }
        readInstructions(input).forEach { instruction ->
            val xRange = instruction.fromX..instruction.toX
            val yRange = instruction.fromY..instruction.toY
            for (y in yRange) {
                for (x in xRange) {
                    when (instruction.type) {
                        InstructionType.TURN_ON -> grid[y][x] += 1
                        InstructionType.TURN_OFF -> grid[y][x] = max(0, grid[y][x] - 1)
                        InstructionType.TOGGLE -> grid[y][x] += 2
                    }
                }
            }
        }
        println(grid.sumOf { row -> row.sumOf { it } })
    }

    private fun readInstructions(input: Input): List<Instruction> =
        input.asLines()
            .map { it.split(' ') }
            .map { values ->
                when (values[0]) {
                    "turn" -> {
                        val (fromX, fromY) = values[2].split(',').map { it.toInt() }
                        val (toX, toY) = values[4].split(',').map { it.toInt() }
                        when (values[1]) {
                            "on" -> Instruction(InstructionType.TURN_ON, fromX, fromY, toX, toY)
                            "off" -> Instruction(InstructionType.TURN_OFF, fromX, fromY, toX, toY)
                            else -> error(values[1])
                        }
                    }
                    "toggle" -> {
                        val (fromX, fromY) = values[1].split(',').map { it.toInt() }
                        val (toX, toY) = values[3].split(',').map { it.toInt() }
                        Instruction(InstructionType.TOGGLE, fromX, fromY, toX, toY)
                    }
                    else -> error(values[0])
                }
            }

    private class Instruction(
        val type: InstructionType,
        val fromX: Int,
        val fromY: Int,
        val toX: Int,
        val toY: Int,
    )

    private enum class InstructionType {
        TURN_ON,
        TURN_OFF,
        TOGGLE
    }

}