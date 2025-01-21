package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day3 : AocDay2 {

    private lateinit var triangles: List<IntArray>

    override fun readInput(input: Input) {
        triangles = input.asLines().map { it.findAllNumbers().toIntArray() }
    }

    override fun part1() {
        println(
            triangles.count { (a, b, c) -> (a + b > c) && (a + c > b) && (b + c > a) }
        )
    }

    override fun part2() {
        var count = 0
        val verticalCount = triangles.size / 3
        repeat(3) { column ->
            repeat(verticalCount) { row ->
                val a = triangles[3 * row][column]
                val b = triangles[3 * row + 1][column]
                val c = triangles[3 * row + 2][column]
                if ((a + b > c) && (a + c > b) && (b + c > a)) count++
            }
        }
        println(count)
    }
}