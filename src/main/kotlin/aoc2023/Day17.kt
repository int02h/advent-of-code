package aoc2023

import AocDay
import Input
import java.util.PriorityQueue

object Day17 : AocDay {
    override fun part1(input: Input) {
        val map = input.asLines().map { line -> line.map { it.digitToInt() } }
        val queue = PriorityQueue<State>()
        queue.add(State(direction = Direction.RIGHT))
        queue.add(State(direction = Direction.DOWN))

        val visited = mutableMapOf<Visited, Int>()
        while (queue.isNotEmpty()) {
            val state = queue.remove()
            val visitedHeatLoss = visited[state.asVisited()]
            if (visitedHeatLoss != null && state.heatLoss >= visitedHeatLoss) {
                continue
            }
            visited[state.asVisited()] = state.heatLoss

            if (state.position.row == map.lastIndex && state.position.col == map[0].lastIndex) {
                println(state.heatLoss)
                break
            }

            if (state.steps < 2) {
                state.next(state.direction, map)?.let(queue::add)
            }
            state.next(state.direction.left(), map)?.let(queue::add)
            state.next(state.direction.right(), map)?.let(queue::add)
        }
    }

    override fun part2(input: Input) {
        val map = input.asLines().map { line -> line.map { it.digitToInt() } }
        val queue = PriorityQueue<State>()
        queue.add(State(direction = Direction.RIGHT))
        queue.add(State(direction = Direction.DOWN))

        val visited = mutableMapOf<Visited, Int>()
        while (queue.isNotEmpty()) {
            val state = queue.remove()
            val visitedHeatLoss = visited[state.asVisited()]
            if (visitedHeatLoss != null && state.heatLoss >= visitedHeatLoss) {
                continue
            }
            visited[state.asVisited()] = state.heatLoss

            if (state.position.row == map.lastIndex && state.position.col == map[0].lastIndex) {
                if (state.steps > 2) {
                    println(state.heatLoss)
                    break
                }
            }

            if (state.steps < 9) {
                state.next(state.direction, map)?.let(queue::add)
            }
            if (state.steps > 2) {
                state.next(state.direction.left(), map)?.let(queue::add)
                state.next(state.direction.right(), map)?.let(queue::add)
            }
        }
    }

    private data class State(
        val position: Position = Position(0, 0),
        val heatLoss: Int = 0,
        val steps: Int = 0,
        val direction: Direction
    ):Comparable<State> {
        fun next(direction: Direction, map: List<List<Int>>): State? {
            val nextPosition = Position(position.row + direction.dRow, position.col + direction.dCol)
            val heatLoss = map.get(nextPosition) ?: return null
            val nextSteps = if (this.direction == direction) steps + 1 else 0
            return State(
                position = nextPosition,
                heatLoss = this.heatLoss + heatLoss,
                steps = nextSteps,
                direction = direction
            )
        }

        fun asVisited(): Visited = Visited(position, steps, direction)

        override fun compareTo(other: State): Int {
            return this.heatLoss - other.heatLoss
        }
    }

    private data class Visited(
        val position: Position,
        val steps: Int,
        val direction: Direction
    )

    private data class Position(val row: Int, val col: Int)

    private enum class Direction(val dRow: Int, val dCol: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        fun left(): Direction =
            when (this) {
                UP -> LEFT
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
            }

        fun right(): Direction =
            when (this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
    }

    private fun List<List<Int>>.get(pos: Position): Int? = getOrNull(pos.row)?.getOrNull(pos.col)

}

