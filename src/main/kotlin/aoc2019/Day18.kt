package aoc2019

import AocDay
import Input
import util.Maze
import util.Maze.Point

object Day18 : AocDay {
    override fun part1(input: Input) {
        val map = input.asLines()
        val (keys, startPoint) = buildKeysAndStartPoint(map)
        val routes = buildAllRoutes(map, keys, startPoint)

        data class State(
            val point: Point,
            val foundKeys: Set<Char>,
            val pathLength: Int
        )

        data class Visited(val point: Point, val foundKeys: Set<Char>)
        val visited = mutableMapOf<Visited, Int>()

        fun walk(state: State): Int {
            if (state.foundKeys.size == keys.size) {
                return state.pathLength
            }
            val v = visited[Visited(state.point, state.foundKeys)]
            if (v != null && v <= state.pathLength) {
                return Int.MAX_VALUE
            }
            visited[Visited(state.point, state.foundKeys)] = state.pathLength

            val availableKeys = getAvailableKeys(state.point, state.foundKeys, keys, routes)
            return availableKeys.minOfOrNull { (key, distance) ->
                walk(
                    State(
                        point = keys.getValue(key),
                        foundKeys = state.foundKeys + key,
                        pathLength = state.pathLength + distance
                    )
                )
            } ?: Int.MAX_VALUE
        }

        val steps = walk(State(point = startPoint, foundKeys = emptySet(), pathLength = 0))
        println(steps)
    }

    override fun part2(input: Input) {
        val map = input.asLines().map { it.toCharArray() }
        val (keys, startPoint) = buildKeysAndStartPoint(input.asLines())
        for (i in -1..1) {
            map[startPoint.y + i][startPoint.x] = '#'
            map[startPoint.y][startPoint.x + i] = '#'
        }
        val routes = mutableMapOf<RouteKey, RouteValue>().apply {
            this += buildAllRoutes(map.map { String(it) }, keys, startPoint.up.left)
            this += buildAllRoutes(map.map { String(it) }, keys, startPoint.up.right)
            this += buildAllRoutes(map.map { String(it) }, keys, startPoint.down.left)
            this += buildAllRoutes(map.map { String(it) }, keys, startPoint.down.right)
        }

        data class State(
            val robots: List<Point>,
            val foundKeys: Set<Char>,
            val pathLength: Int
        )

        data class Visited(val robots: List<Point>, val foundKeys: Set<Char>)
        val visited = mutableMapOf<Visited, Int>()

        fun walk(state: State): Int {
            if (state.foundKeys.size == keys.size) {
                return state.pathLength
            }
            val v = visited[Visited(state.robots, state.foundKeys)]
            if (v != null && v <= state.pathLength) {
                return Int.MAX_VALUE
            }
            visited[Visited(state.robots, state.foundKeys)] = state.pathLength

            val result = state.robots.withIndex().minOf { (index, robot) ->
                val availableKeys = getAvailableKeys(robot, state.foundKeys, keys, routes)
                availableKeys.minOfOrNull { (key, distance) ->
                    walk(
                        State(
                            robots = state.robots.replaceItem(index, keys.getValue(key)),
                            foundKeys = state.foundKeys + key,
                            pathLength = state.pathLength + distance
                        )
                    )
                } ?: Int.MAX_VALUE
            }
            return result
        }

        val steps = walk(
            State(
                robots = listOf(
                    startPoint.up.left,
                    startPoint.up.right,
                    startPoint.down.left,
                    startPoint.down.right,
                ),
                foundKeys = emptySet(),
                pathLength = 0
            )
        )
        println(steps)
    }

    private fun buildKeysAndStartPoint(map: List<String>): Pair<Map<Char, Point>, Point> {
        val keys = mutableMapOf<Char, Point>()
        lateinit var startPoint: Point
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch in 'a'..'z') {
                    keys[map[y][x]] = Point(x, y)
                }
                if (ch == '@') {
                    startPoint = Point(x, y)
                }
            }
        }
        return keys to startPoint
    }

    private fun buildAllRoutes(
        map: List<String>,
        keys: Map<Char, Point>,
        startPoint: Point
    ): Map<RouteKey, RouteValue> {
        val routes = mutableMapOf<RouteKey, RouteValue>()
        val maze = Maze { p ->
            val ch = map[p.y][p.x]
            ch == '.' || ch == '@' || ch.isLetter()
        }
        val fromKeys = keys.toMutableMap().apply {
            put('@', startPoint)
        }
        fromKeys.forEach { (fromKey, fromPosition) ->
            keys.forEach { (toKey, toPosition) ->
                if (fromKey != toKey) {
                    val route = maze.findShortestRoute(fromPosition, toPosition)
                    if (route.isNotEmpty()) {
                        val doors = route.mapNotNull { p ->
                            val cell = map[p.y][p.x]
                            if (cell in 'A'..'Z') cell else null
                        }.toSet()
                        routes[RouteKey(fromPosition, toPosition)] = RouteValue(route.size - 1, doors)
                    }
                }
            }
        }
        return routes
    }

    private fun getAvailableKeys(
        fromPosition: Point,
        foundKeys: Set<Char>,
        allKeys: Map<Char, Point>,
        allRoutes: Map<RouteKey, RouteValue>,
    ): List<Pair<Char, Int>> {
        val result = mutableListOf<Pair<Char, Int>>()
        for ((key, keyPosition) in allKeys) {
            if (foundKeys.contains(key)) {
                continue
            }
            val routeKey = RouteKey(fromPosition, keyPosition)
            val cached = allRoutes[routeKey]
            if (cached != null && cached.doors.all { foundKeys.contains(it.lowercaseChar()) }) {
                result += key to cached.pathLength
            }
        }
        return result
    }

    private fun <E> List<E>.replaceItem(index: Int, value: E): List<E> = toMutableList().apply { set(index, value) }

    data class RouteKey(val fromPosition: Point, val toPosition: Point)
    data class RouteValue(val pathLength: Int, val doors: Set<Char>)

}
