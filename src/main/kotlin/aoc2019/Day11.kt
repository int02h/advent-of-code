package aoc2019

import AocDay
import Input

object Day11 : AocDay {

    override fun part1(input: Input) {
        val panels = mutableMapOf<Position, Long>()
        paint(input, panels)
        println(panels.size)
    }

    override fun part2(input: Input) {
        val panels = mutableMapOf<Position, Long>()
        panels[Position(0, 0)] = 1
        paint(input, panels)

        val minX = panels.minOf { it.key.x }
        val maxX = panels.maxOf { it.key.x }
        val minY = panels.minOf { it.key.y }
        val maxY = panels.maxOf { it.key.y }

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val p = Position(x, y)
                if (panels.getOrDefault(p, 0) == 1L) {
                    print('#')
                } else {
                    print(' ')
                }
            }
            println()
        }
    }

    private fun paint(input: Input, panels: MutableMap<Position, Long>) {
        val programInput = mutableListOf<Long>()
        val programOutput = mutableListOf<Long>()
        val computer = IntCodeComputer(
            program = readProgram(input),
            input = programInput,
            output = programOutput
        )
        val robot = Robot()
        while (true) {
            val color = panels.getOrDefault(robot.position, 0)
            programInput += color
            computer.runUntilOutput(2)
            if (programOutput.isEmpty()) {
                break
            }
            val nextColor = programOutput.removeFirst()
            val turn = programOutput.removeFirst()
            panels[robot.position] = nextColor
            robot.turnAndMove(turn)
        }
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()

    private data class Position(val x: Long, val y: Long)

    private class Robot {
        var position = Position(0, 0)
        var direction: Direction = Direction.UP

        fun turnAndMove(turn: Long) {
            val isLeft = (turn == 0L)
            direction = when (direction) {
                Direction.UP -> if (isLeft) Direction.LEFT else Direction.RIGHT
                Direction.DOWN -> if (isLeft) Direction.RIGHT else Direction.LEFT
                Direction.LEFT -> if (isLeft) Direction.DOWN else Direction.UP
                Direction.RIGHT -> if (isLeft) Direction.UP else Direction.DOWN
            }
            position = when (direction) {
                Direction.UP -> position.copy(y = position.y - 1)
                Direction.DOWN -> position.copy(y = position.y + 1)
                Direction.LEFT -> position.copy(x = position.x - 1)
                Direction.RIGHT -> position.copy(x = position.x + 1)
            }
        }
    }

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
}