package aoc2024

import AocDay2
import Input
import util.RowColPosition
import java.util.PriorityQueue
import util.RowColPosition as Position


class Day16 : AocDay2 {

    private lateinit var map: List<String>
    private lateinit var start: Position
    private lateinit var end: Position

    override fun readInput(input: Input) {
        map = input.asLines()
        map.forEachIndexed { row, line ->
            line.forEachIndexed { col, cell ->
                if (cell == 'S') start = Position(row, col)
                if (cell == 'E') end = Position(row, col)
            }
        }
    }

    override fun part1() {
        val route = findShortestRoute(start, end)
        println(calcScore(route))
    }

    override fun part2() {
        val bestScore = calcScore(findShortestRoute(start, end))
        val positions = mutableSetOf<Position>()
        val threads = mutableListOf<Thread>()

        for ((row, line) in map.withIndex()) {
            val t = Thread {
                for ((col, cell) in line.withIndex()) {
                    if (row == 10 && col == 1) {
                        Unit
                    }
                    if (cell != '#') {
                        val p = Position(row, col)
                        val r1 = findShortestRoute(start, p)
                        val skip = when (r1.lastOrNull()) {
                            '^' -> p.down
                            'v' -> p.up
                            '<' -> p.right
                            '>' -> p.left
                            else -> null
                        }
                        val r2 = findShortestRoute(p, end, skip = skip)
                        if (calcScore(r1 + r2) == bestScore) {
                            synchronized(positions) {
                                positions += p
                            }
                        }
                    }
                }
                println("Row: $row")
            }
            threads += t
            t.start()
        }

        threads.forEach { it.join() }
        println(positions.size)
    }

    private fun findShortestRoute(
        from: Position,
        to: Position,
        skip: Position? = null,
    ): String {
        if (from == to) return ""
        val visited: MutableMap<RowColPosition, Int> = mutableMapOf()
        val queue = PriorityQueue<RoutePoint> { p1, p2 -> calcScore(p1.currentRoute) - calcScore(p2.currentRoute) }
        queue.add(RoutePoint(from, ""))

        while (queue.isNotEmpty()) {
            val routePoint = queue.remove()
            if (skip == routePoint.point) {
                continue
            }
            val v = visited[routePoint.point]
            if (v != null && v <= calcScore(routePoint.currentRoute)) {
                continue
            }
            if (routePoint.point == to) {
                return routePoint.currentRoute
            }
            visited[routePoint.point] = calcScore(routePoint.currentRoute)
            if (isEmptyCell(routePoint.point.up)) queue.add(routePoint.up())
            if (isEmptyCell(routePoint.point.down)) queue.add(routePoint.down())
            if (isEmptyCell(routePoint.point.left)) queue.add(routePoint.left())
            if (isEmptyCell(routePoint.point.right)) queue.add(routePoint.right())
        }
        return ""
    }

    private fun isEmptyCell(p: Position): Boolean = map[p.row][p.col] != '#'

    private fun calcScore(route: String): Int {
        var score = 0
        var prevMovement = '>'
        route.forEach { movement ->
            if (prevMovement != movement) {
                score += 1000
            }
            score++
            prevMovement = movement
        }
        return score
    }

    private data class RoutePoint(val point: Position, val currentRoute: String) {
        fun up(): RoutePoint = RoutePoint(point.up, currentRoute + "^")
        fun down(): RoutePoint = RoutePoint(point.down, currentRoute + "v")
        fun left(): RoutePoint = RoutePoint(point.left, currentRoute + "<")
        fun right(): RoutePoint = RoutePoint(point.right, currentRoute + ">")
    }

}