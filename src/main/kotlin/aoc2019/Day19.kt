package aoc2019

import AocDay
import Input
import util.Maze

object Day19 : AocDay {
    override fun part1(input: Input) {
        var count = 0
        for (y in 0L until 50L) {
            for (x in 0L until 50L) {
                val program = readProgram(input)
                val programInput = mutableListOf<Long>()
                val programOutput = mutableListOf<Long>()
                val computer = IntCodeComputer(program = program, input = programInput, output = programOutput)
                programInput.add(x)
                programInput.add(y)
                computer.runAll()
                if (programOutput.first() == 1L) {
                    count++
                }
            }
        }
        println(count)
    }

    override fun part2(input: Input) {
        val program = readProgram(input)
        val programInput = mutableListOf<Long>()
        val programOutput = mutableListOf<Long>()
        val map = mutableMapOf<Maze.Point, Long>()

        fun checkArea(startX: Int, startY: Int): Boolean {
            for (y in startY until (startY + 100)) {
                for (x in startX until (startX + 100)) {
                    var cell = map[Maze.Point(x, y)]
                    if (cell == null) {
                        val computer = IntCodeComputer(program = program, input = programInput, output = programOutput)
                        programInput.clear()
                        programInput.add(x.toLong())
                        programInput.add(y.toLong())
                        programOutput.clear()
                        computer.runAll()
                        cell = programOutput.first()
                        map[Maze.Point(x, y)] = cell
                        programOutput.first()
                    }
                    if (cell == 0L) {
                        return false
                    }
                }
            }
            return true
        }

        for (y in 100..10_000) {
            val prevLine = map.filter { it.key.y == y - 1 && it.value == 1L }
            val minX = prevLine.minOfOrNull { it.key.x } ?: 0
            val maxX = prevLine.maxOfOrNull { it.key.x } ?: 10_000
            for (x in minX..(maxX + 10)) {
                if (checkArea(x, y)) {
                    println(x * 10_000 + y)
                    return
                }
            }
        }
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
}