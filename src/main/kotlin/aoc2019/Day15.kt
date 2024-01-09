package aoc2019

import AocDay
import Input
import util.Maze
import java.util.Stack

object Day15 : AocDay {
    override fun part1(input: Input) {
        val map = buildMap(input)
        val oxygenPoint = map.filter { it.value == 'T' }.keys.first()
        val maze = Maze { p -> map[p] != '#' }
        val route = maze.findShortestRoute(Maze.Point(0, 0), oxygenPoint)
        printMap(map, route)
        println(route.size - 1)
    }

    override fun part2(input: Input) {
        val map = buildMap(input)
        val oxygenPoint = map.filter { it.value == 'T' }.keys.first()
        val maze = Maze { p -> map[p] != '#' }
        val longestRoute = maze.findLongestRoute(oxygenPoint)
        printMap(map, longestRoute)
        println(longestRoute.size - 1)
    }

    private fun buildMap(input: Input): Map<Maze.Point, Char> {
        val droid = Droid(readProgram(input))
        val map = mutableMapOf<Maze.Point, Char>()

        fun walk(point: Maze.Point) {
            map[point] = '.'
            if (!map.containsKey(point.up)) {
                when (droid.move(Direction.UP)) {
                    MovementResult.WALL -> map[point.up] = '#'
                    MovementResult.SUCCESS -> {
                        walk(point.up)
                        droid.moveBack()
                    }
                    MovementResult.TARGET -> {
                        map[point.up] = 'T'
                        droid.moveBack()
                    }
                }
            }
            if (!map.containsKey(point.down)) {
                when (droid.move(Direction.DOWN)) {
                    MovementResult.WALL -> map[point.down] = '#'
                    MovementResult.SUCCESS -> {
                        walk(point.down)
                        droid.moveBack()
                    }
                    MovementResult.TARGET -> {
                        map[point.down] = 'T'
                        droid.moveBack()
                    }
                }
            }
            if (!map.containsKey(point.left)) {
                when (droid.move(Direction.LEFT)) {
                    MovementResult.WALL -> map[point.left] = '#'
                    MovementResult.SUCCESS -> {
                        walk(point.left)
                        droid.moveBack()
                    }
                    MovementResult.TARGET -> {
                        map[point.left] = 'T'
                        droid.moveBack()
                    }
                }
            }
            if (!map.containsKey(point.right)) {
                when (droid.move(Direction.RIGHT)) {
                    MovementResult.WALL -> map[point.right] = '#'
                    MovementResult.SUCCESS -> {
                        walk(point.right)
                        droid.moveBack()
                    }
                    MovementResult.TARGET -> {
                        map[point.right] = 'T'
                        droid.moveBack()
                    }
                }
            }
        }
        walk(Maze.Point(0, 0))
        return map
    }

    private fun printMap(map: Map<Maze.Point, Char>, route: List<Maze.Point>) {
        val minY = map.minOf { it.key.y }
        val maxY = map.maxOf { it.key.y }
        val minX = map.minOf { it.key.x }
        val maxX = map.maxOf { it.key.x }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (route.contains(Maze.Point(x, y))) {
                    if (x == 0 && y == 0) print('S')
                    else print('*')
                } else {
                    print(map[Maze.Point(x, y)] ?: ' ')
                }
            }
            println()
        }
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()

    private class Droid(program: LongArray) {
        private val programInput = mutableListOf<Long>()
        private val programOutput = mutableListOf<Long>()
        private val computer = IntCodeComputer(
            program = program,
            input = programInput,
            output = programOutput
        )

        private val movementStack = Stack<Direction>()

        fun move(direction: Direction): MovementResult {
            programInput += direction.value
            computer.runUntilOutput()
            val result = MovementResult.values()[programOutput.removeFirst().toInt()]
            if (result != MovementResult.WALL) {
                movementStack.push(direction)
            }
            return result
        }

        fun moveBack() {
            val direction = movementStack.pop()
            val reverted = direction.revert()
            programInput += reverted.value
            computer.runUntilOutput()
            assert(programOutput.removeFirst() == 1L)
        }
    }

    private enum class Direction(val value: Long) {
        UP(1L) {
            override fun revert(): Direction = DOWN
        },
        DOWN(2L) {
            override fun revert(): Direction = UP
        },
        LEFT(3L) {
            override fun revert(): Direction = RIGHT
        },
        RIGHT(4L) {
            override fun revert(): Direction = LEFT
        };

        abstract fun revert(): Direction
    }

    private enum class MovementResult {
        WALL,
        SUCCESS,
        TARGET
    }
}