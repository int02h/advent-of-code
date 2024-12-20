package aoc2024

import AocDay2
import Input
import util.Maze
import kotlin.math.abs

class Day20 : AocDay2 {

    private lateinit var map: List<String>
    private lateinit var start: Maze.Point
    private lateinit var end: Maze.Point

    private val distanceFromStart = mutableMapOf<Maze.Point, Int>()
    private val distanceToEnd = mutableMapOf<Maze.Point, Int>()

    val maze = Maze { p -> map[p.y][p.x] != '#' }

    override fun readInput(input: Input) {
        map = input.asLines()
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, cell ->
                when (cell) {
                    'S' -> start = Maze.Point(x, y)
                    'E' -> end = Maze.Point(x, y)
                }
            }
        }
    }

    override fun part1() {
        exec(2)
    }

    override fun part2() {
        exec(20)
    }

    private fun exec(maxDistance: Int) {
        buildDistanceCache()
        println("Cache ready")
        val originalDistance = maze.findShortestRouteLength(start, end)
        var count = 0
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '#') {
                    continue
                }
                val p = Maze.Point(x, y)
                val reachable = getReachable(x, y, maxDistance)
                reachable.forEach { r ->
                    val cheatCost = abs(x - r.x) + abs(y - r.y)
                    distanceFromStart[p]?.let { ds ->
                        distanceToEnd[r]?.let { de ->
                            val newDistance = ds + cheatCost + de
                            val saving = originalDistance - newDistance
                            if (saving >= 100) {
                                count++
                            }
                        }
                    }
                }
            }
        }
        println(count)
    }

    private fun getReachable(x: Int, y: Int, maxDistance: Int): List<Maze.Point> {
        val result = mutableListOf<Maze.Point>()
        for (dx in -maxDistance..maxDistance) {
            val remainingDistance = maxDistance - abs(dx)
            for (dy in -remainingDistance..remainingDistance) {
                if (dx == 0 && dy == 0) continue
                val nx = x + dx
                val ny = y + dy
                if (nx >= 0 && nx < map[0].length && ny >= 0 && ny < map.size && map[ny][nx] != '#') {
                    result += Maze.Point(nx, ny)
                }
            }
        }
        return result
    }

    private fun buildDistanceCache() {
        for (y in map.indices) {
            print("$y             \r")
            for (x in map[y].indices) {
                if (map[y][x] != '#') {
                    val p = Maze.Point(x, y)
                    var route = maze.findShortestRouteLength(start, p)
                    if (route != Int.MAX_VALUE) {
                        distanceFromStart[p] = route
                    }
                    route = maze.findShortestRouteLength(end, p)
                    if (route != Int.MAX_VALUE) {
                        distanceToEnd[p] = route
                    }
                }
            }
        }
    }

}