package aoc2022

import Input
import java.util.Stack
import kotlin.math.abs

object Day24 {

    fun part1(input: Input) {
        val (map, initBlizzards) = readInput(input)
        val mapStates = buildMapState(map, initBlizzards)
        val (startPosition, endPosition) = getPositions(map)

        println(findBestPath(startPosition, endPosition, map, mapStates).first)
    }

    fun part2(input: Input) {
        val (map, initBlizzards) = readInput(input)
        val mapStates = buildMapState(map, initBlizzards).toMutableList()
        val (startPosition, endPosition) = getPositions(map)

        val (best1, mapIndex1) = findBestPath(startPosition, endPosition, map, mapStates)
        mapStates.shiftLeft(mapIndex1)
        val (best2, mapIndex2) = findBestPath(endPosition, startPosition, map, mapStates)
        mapStates.shiftLeft(mapIndex2)
        val (best3, _) = findBestPath(startPosition, endPosition, map, mapStates)
        println(best1 + best2 + best3)
    }

    private fun getPositions(map: Map<Position, Char>): Pair<Position, Position> {
        val startX = map.filter { it.key.y == 0 && it.value == '.' }.keys.first().x
        val startPosition = Position(startX, 0)

        val endY = map.keys.maxOf { it.y }
        val endX = map.filter { it.key.y == endY && it.value == '.' }.keys.first().x
        val endPosition = Position(endX, endY)

        return startPosition to endPosition
    }

    private fun buildMapState(map: Map<Position, Char>, initBlizzards: List<Blizzard>): List<Set<Position>> {
        val list = mutableListOf<Set<Position>>()
        var blizzards = initBlizzards
        while (list.indexOf(blizzards.map { Position(it.x, it.y) }.toSet()) == -1) {
            list.add(blizzards.map { Position(it.x, it.y) }.toSet())
            blizzards = moveBlizzards(blizzards, map)
        }
        return list
    }

    private fun findBestPath(
        startPosition: Position,
        endPosition: Position,
        map: Map<Position, Char>,
        mapStates: List<Set<Position>>
    ): Pair<Int, Int> {
        val stack = Stack<State>()
        stack.push(State(startPosition, 0))
        var best = map.size
        var bestMapStateIndex: Int = -1

        val seen = mutableSetOf<String>()

        while (stack.isNotEmpty()) {
            val state = stack.pop()
            val player = state.player
            val mapStateIndex = state.time % mapStates.size
            if (player == endPosition) {
                if (state.time < best) {
                    best = state.time
                    bestMapStateIndex = mapStateIndex
                }
            }
            if (mapStates[mapStateIndex].contains(player)) {
                continue
            }
            if (state.time + player.distanceTo(endPosition) >= best) {
                continue
            }

            val seenKey = "${state.time}:${state.player}:${mapStateIndex}"
            if (seen.contains(seenKey)) {
                continue
            }
            seen.add(seenKey)

            stack.push(State(player, state.time + 1))
            if (map[player.up] == '.') {
                stack.push(State(player.up, state.time + 1))
            }
            if (map[player.down] == '.') {
                stack.push(State(player.down, state.time + 1))
            }
            if (map[player.left] == '.') {
                stack.push(State(player.left, state.time + 1))
            }
            if (map[player.right] == '.') {
                stack.push(State(player.right, state.time + 1))
            }
        }

        return best to bestMapStateIndex
    }

    private fun moveBlizzards(blizzards: List<Blizzard>, map: Map<Position, Char>): List<Blizzard> =
        blizzards.map { b -> b.move(map) }

    private fun readInput(input: Input): Pair<Map<Position, Char>, List<Blizzard>> {
        val map = mutableMapOf<Position, Char>()
        val blizzards = mutableListOf<Blizzard>()
        var y = 0
        input.asLines().forEach { line ->
            var x = 0
            line.forEach { ch ->
                when (ch) {
                    '#', '.' -> map[Position(x, y)] = ch
                    '>', '<', '^', 'v' -> {
                        blizzards += Blizzard(x, y, ch)
                        map[Position(x, y)] = '.'
                    }
                }
                x++
            }
            y++
        }
        return map to blizzards
    }

    private data class Position(val x: Int, val y: Int) {
        val up: Position get() = copy(y = y - 1)
        val down: Position get() = copy(y = y + 1)
        val left: Position get() = copy(x = x - 1)
        val right: Position get() = copy(x = x + 1)

        fun distanceTo(other: Position): Int = abs(other.x - this.x) + abs(other.y - this.y)
    }

    private data class Blizzard(val x: Int, val y: Int, val direction: Char) {

        fun move(map: Map<Position, Char>): Blizzard {
            var nextX = x
            var nextY = y
            when (direction) {
                '<' -> {
                    nextX = x - 1
                    if (map[Position(nextX, y)] == '#') {
                        nextX = map.keys.filter { it.y == y }.maxOf { it.x } - 1
                    }
                }
                '>' -> {
                    nextX = x + 1
                    if (map[Position(nextX, y)] == '#') {
                        nextX = map.keys.filter { it.y == y }.minOf { it.x } + 1
                    }
                }
                '^' -> {
                    nextY = y - 1
                    if (map[Position(x, nextY)] == '#') {
                        nextY = map.keys.filter { it.x == x }.maxOf { it.y } - 1
                    }
                }
                'v' -> {
                    nextY = y + 1
                    if (map[Position(x, nextY)] == '#') {
                        nextY = map.keys.filter { it.x == x }.minOf { it.y } + 1
                    }
                }
            }
            return Blizzard(nextX, nextY, direction)
        }

    }

    private data class State(
        val player: Position,
        val time: Int,
    )

    private fun <E> MutableList<E>.shiftLeft(count: Int) {
        repeat(count) {
            add(removeAt(0))
        }
    }
}
