package aoc2023

import AocDay
import Input
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day18 : AocDay {

    override fun part1(input: Input) {
        val lines = getLines(input)
        println(getLagoonSize(lines))
    }

    override fun part2(input: Input) {
        val lines = getLines(input, isPart2 = true)
        println(getLagoonSize(lines))
    }

    private fun getLagoonSize(points: List<Point>): Long {
        var sum = 0L
        var j = points.lastIndex
        var perimeter = 0L
        for (i in 0..points.lastIndex) {
            sum += (points[j].y.toLong() + points[i].y) * (points[j].x - points[i].x)
            perimeter += abs(points[j].x.toLong() - points[i].x) + abs(points[j].y - points[i].y) // perimeter
            j = i
        }
        return (sum / 2) + perimeter / 2 + 1
    }

    private fun getLines(input: Input, isPart2: Boolean = false): List<Point> {
        var x = 0
        var y = 0
        val points = mutableListOf<Point>()
        input.asLines().forEach {
            val (directionRaw, countRaw, colorRaw) = it.split(" ")
            val (count, direction) = if (isPart2) {
                val color = colorRaw.substring(2, colorRaw.lastIndex)
                color.dropLast(1).toInt(16) to when (color.last()) {
                    '0' -> "R"
                    '1' -> "D"
                    '2' -> "L"
                    '3' -> "U"
                    else -> error("Impossible")
                }
            } else {
                countRaw.toInt() to directionRaw
            }
            when (direction) {
                "U" -> y -= count
                "D" -> y += count
                "L" -> x -= count
                "R" -> x += count
            }
            points += Point(x, y)
        }
        return points
    }

    private data class Point(val x: Int, val y: Int)
}