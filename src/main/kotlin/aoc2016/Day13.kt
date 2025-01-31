package aoc2016

import AocDay2
import Input
import util.Maze

class Day13 : AocDay2 {

    private var favoriteNumber = 0

    override fun readInput(input: Input) {
        favoriteNumber = input.asText().trim().toInt()
    }

    override fun part1() {
        val maze = Maze { p ->
            if (p.x < 0 || p.y < 0) {
                false
            } else {
                val value = p.x * p.x + 3 * p.x + 2 * p.x * p.y + p.y + p.y * p.y + favoriteNumber
                value.countOneBits() % 2 == 0
            }
        }
        println(maze.findShortestRouteLength(Maze.Point(1, 1), Maze.Point(31, 39)))
    }

    override fun part2() {
        val maze = Maze { p ->
            if (p.x < 0 || p.y < 0) {
                false
            } else {
                val value = p.x * p.x + 3 * p.x + 2 * p.x * p.y + p.y + p.y * p.y + favoriteNumber
                value.countOneBits() % 2 == 0
            }
        }
        val result = mutableSetOf<Maze.Point>()
        for (i in 1..50) {
            val routes = maze.findAllRoutesOfLength(Maze.Point(1, 1), i)
            result += routes.flatten()
        }
        println(result.size)
    }
}

