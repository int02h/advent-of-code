package aoc2024

import AocDay2
import Input
import util.Maze

class Day18 : AocDay2 {

    private lateinit var bytePositions: List<Maze.Point>

    override fun readInput(input: Input) {
        bytePositions = input.asLines()
            .map { line ->
                val (x, y) = line.split(",").map { it.toInt() }
                Maze.Point(x, y)
            }
    }

    override fun part1() {
        val map = mutableSetOf<Maze.Point>()
        repeat(1024) { map += bytePositions[it] }
        val maze = Maze { p ->
            if (p.x > 70 || p.x < 0 || p.y > 70 || p.y < 0) {
                false
            } else {
                !map.contains(p)
            }
        }
        val route = maze.findShortestRoute(Maze.Point(0, 0), Maze.Point(70, 70))
        println(route.size - 1)
    }

    override fun part2() {
        val map = mutableSetOf<Maze.Point>()
        repeat(1024) { map += bytePositions[it] }
        val maze = Maze { p ->
            if (p.x > 70 || p.x < 0 || p.y > 70 || p.y < 0) {
                false
            } else {
                !map.contains(p)
            }
        }
        var byteIndex = 1024
        while (true) {
            val pos = bytePositions[byteIndex]
            map += pos
            val route = maze.findShortestRoute(Maze.Point(0, 0), Maze.Point(70, 70))
            if (route.isEmpty()) {
                println("${pos.x},${pos.y}")
                break
            }
            byteIndex++
        }
    }
}