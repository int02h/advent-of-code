package aoc2024

import AocDay
import Input
import java.util.PriorityQueue
import util.RowColPosition as Position

object Day10 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines().map { line -> line.map { it.digitToInt() } }
        val trailheads = map.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, cell ->
                if (cell == 0) Position(row, col) else null
            }
        }

        println(
            trailheads.sumOf { th -> searchTrails(th, map) }
        )
    }

    override fun part2(input: Input) {
        val map = input.asLines().map { line -> line.map { it.digitToInt() } }
        val trailheads = map.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, cell ->
                if (cell == 0) Position(row, col) else null
            }
        }
        val peaks = map.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, cell ->
                if (cell == 9) Position(row, col) else null
            }
        }

        println(
            trailheads.sumOf { th ->
                val allTrails = mutableListOf<List<Position>>()
                peaks.forEach { p -> searchAllTrails(th, p, map, allTrails) }
                allTrails.size
            }
        )
    }

    private fun searchTrails(trailhead: Position, map: List<List<Int>>): Int {
        val visited = mutableMapOf<Position, Int>()
        val queue = PriorityQueue<RoutePoint> { p1, p2 -> p2.routeLength - p1.routeLength }
        queue.add(RoutePoint(trailhead, 0))

        val peaks = mutableSetOf<Position>()
        while (queue.isNotEmpty()) {
            val routePoint = queue.remove()
            if (map[routePoint.position.row][routePoint.position.col] == 9) {
                peaks += routePoint.position
                continue
            }
            val v = visited[routePoint.position]
            if (v != null) {
                continue
            }
            visited[routePoint.position] = routePoint.routeLength
            if (canMove(routePoint.position, routePoint.position.up, map)) queue.add(routePoint.up())
            if (canMove(routePoint.position, routePoint.position.down, map)) queue.add(routePoint.down())
            if (canMove(routePoint.position, routePoint.position.left, map)) queue.add(routePoint.left())
            if (canMove(routePoint.position, routePoint.position.right, map)) queue.add(routePoint.right())
        }

        return peaks.size
    }

    private fun searchAllTrails(
        from: Position,
        to: Position,
        map: List<List<Int>>,
        result: MutableList<List<Position>>,
        visited: MutableSet<Position> = mutableSetOf(),
        localPath: MutableList<Position> = mutableListOf(),
    ) {
        if (from == to) {
            result += localPath.toMutableList()
            return
        }
        visited += from
        from.up.let { p ->
            if (canMove(from, p, map) && !visited.contains(p)) {
                localPath += p
                searchAllTrails(p, to, map, result, visited, localPath)
                localPath -= p
            }
        }
        from.down.let { p ->
            if (canMove(from, p, map) && !visited.contains(p)) {
                localPath += p
                searchAllTrails(p, to, map, result, visited, localPath)
                localPath -= p
            }
        }
        from.left.let { p ->
            if (canMove(from, p, map) && !visited.contains(p)) {
                localPath += p
                searchAllTrails(p, to, map, result, visited, localPath)
                localPath -= p
            }
        }
        from.right.let { p ->
            if (canMove(from, p, map) && !visited.contains(p)) {
                localPath += p
                searchAllTrails(p, to, map, result, visited, localPath)
                localPath -= p
            }
        }
        visited -= from
    }

    private fun canMove(from: Position, to: Position, map: List<List<Int>>): Boolean {
        val diff = (map.getOrNull(to.row)?.getOrNull(to.col) ?: 0) - map[from.row][from.col]
        return diff == 1
    }

    private data class RoutePoint(val position: Position, val routeLength: Int) {
        fun up(): RoutePoint = RoutePoint(position.up, routeLength + 1)
        fun down(): RoutePoint = RoutePoint(position.down, routeLength + 1)
        fun left(): RoutePoint = RoutePoint(position.left, routeLength + 1)
        fun right(): RoutePoint = RoutePoint(position.right, routeLength + 1)
    }
}