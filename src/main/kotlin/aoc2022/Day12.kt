package aoc2022

import Input
import java.util.Stack
import kotlin.math.min

object Day12 {

    fun part1(input: Input) {
        val map = readHeightMap(input)
        val stack = Stack<Position>()
        stack.push(map.startPos)

        val routes = Array(map.heights.size) { IntArray(map.heights.first().size) { Int.MAX_VALUE } }
        val routeStack = Stack<Int>()
        routeStack.push(0)

        while (stack.isNotEmpty()) {
            val pos = stack.pop()
            val curRoute = routeStack.pop()
            routes[pos.y][pos.x] = curRoute
            val curHeight = map.heights[pos.y][pos.x]
            map.heights.getOrNull(pos.y)?.getOrNull(pos.x + 1)?.let { cell ->
                if (cell <= curHeight + 1 && curRoute + 1 < routes[pos.y][pos.x + 1]) {
                    stack.push(pos.copy(x = pos.x + 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y)?.getOrNull(pos.x - 1)?.let { cell ->
                if (cell <= curHeight + 1 && curRoute + 1 < routes[pos.y][pos.x - 1]) {
                    stack.push(pos.copy(x = pos.x - 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y + 1)?.getOrNull(pos.x)?.let { cell ->
                if (cell <= curHeight + 1 && curRoute + 1 < routes[pos.y + 1][pos.x]) {
                    stack.push(pos.copy(y = pos.y + 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y - 1)?.getOrNull(pos.x)?.let { cell ->
                if (cell <= curHeight + 1 && curRoute + 1 < routes[pos.y - 1][pos.x]) {
                    stack.push(pos.copy(y = pos.y - 1))
                    routeStack.push(curRoute + 1)
                }
            }
        }
        println(routes[map.endPos.y][map.endPos.x])
    }

    fun part2(input: Input) {
        val map = readHeightMap(input)
        val stack = Stack<Position>()
        stack.push(map.endPos)

        val routes = Array(map.heights.size) { IntArray(map.heights.first().size) { Int.MAX_VALUE } }
        val routeStack = Stack<Int>()
        routeStack.push(0)

        var best = Int.MAX_VALUE
        while (stack.isNotEmpty()) {
            val pos = stack.pop()
            val curRoute = routeStack.pop()
            if (map.heights[pos.y][pos.x] == 0) {
                best = min(curRoute, best)
            }
            routes[pos.y][pos.x] = curRoute
            val curHeight = map.heights[pos.y][pos.x]
            map.heights.getOrNull(pos.y)?.getOrNull(pos.x + 1)?.let { cell ->
                if (cell >= curHeight - 1 && curRoute + 1 < routes[pos.y][pos.x + 1]) {
                    stack.push(pos.copy(x = pos.x + 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y)?.getOrNull(pos.x - 1)?.let { cell ->
                if (cell >= curHeight - 1 && curRoute + 1 < routes[pos.y][pos.x - 1]) {
                    stack.push(pos.copy(x = pos.x - 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y + 1)?.getOrNull(pos.x)?.let { cell ->
                if (cell >= curHeight - 1 && curRoute + 1 < routes[pos.y + 1][pos.x]) {
                    stack.push(pos.copy(y = pos.y + 1))
                    routeStack.push(curRoute + 1)
                }
            }
            map.heights.getOrNull(pos.y - 1)?.getOrNull(pos.x)?.let { cell ->
                if (cell >= curHeight - 1 && curRoute + 1 < routes[pos.y - 1][pos.x]) {
                    stack.push(pos.copy(y = pos.y - 1))
                    routeStack.push(curRoute + 1)
                }
            }
        }
        println(best)
    }

    private fun readHeightMap(input: Input): HeightMap {
        var sx = -1
        var sy = -1
        var ex = -1
        var ey = -1
        var x = 0
        var y = 0
        val map = mutableListOf<MutableList<Int>>()
        input.asLines().forEach { line ->
            val mapLine = mutableListOf<Int>()
            line.forEach { cell ->
                when (cell) {
                    'S' -> {
                        sx = x
                        sy = y
                        mapLine += 0
                    }
                    'E' -> {
                        ex = x
                        ey = y
                        mapLine += 'z' - 'a'
                    }
                    else -> mapLine += cell - 'a'
                }
                x++
            }
            map += mapLine
            y++
            x = 0
        }
        return HeightMap(
            startPos = Position(sx, sy),
            endPos = Position(ex, ey),
            heights = map
        )
    }

    class HeightMap(
        val startPos: Position,
        val endPos: Position,
        val heights: List<List<Int>>
    )

    data class Position(val x: Int, val y: Int)
}