package aoc2022

import Input
import kotlin.math.sign

object Day14 {

    fun part1(input: Input) {
        val map = buildRockMap(input)

        val cave = Cave(map, Point(500, 0))
        while (cave.doStep()) {
            //cave.print()
        }

        println(cave.restSandSet.size)
    }

    fun part2(input: Input) {
        val map = buildRockMap(input)
        val cave = Cave(map, Point(500, 0), hasFloor = true)
        while (cave.doStep()) {
            //cave.print()
        }

        println(cave.restSandSet.size)
    }

    private fun buildRockMap(input: Input): Map<Point, Char> {
        val map = mutableMapOf<Point, Char>()
        input.asLines()
            .map { line -> line.split(" -> ") }
            .map { points -> points.map { p -> p.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) } } }
            .forEach { pointList ->
                for (i in 0 until pointList.lastIndex) {
                    putLine(map, pointList[i], pointList[i + 1])
                }
            }
        return map
    }

    private fun putLine(map: MutableMap<Point, Char>, fromPoint: Point, toPoint: Point) {
        val dx = (toPoint.x - fromPoint.x).sign
        val dy = (toPoint.y - fromPoint.y).sign
        var p = fromPoint
        while (p != toPoint) {
            map[p] = '#'
            p = p.copy(x = p.x + dx, y = p.y + dy)
        }
        map[toPoint] = '#'
    }

    private data class Point(val x: Int, val y: Int) {
        val below: Point get() = Point(x, y + 1)
        val belowLeft: Point get() = Point(x - 1, y + 1)
        val belowRight: Point get() = Point(x + 1, y + 1)
    }

    private class Cave(
        map: Map<Point, Char>,
        private val startSandPoint: Point,
        private val hasFloor: Boolean = false
    ) {
        private val map = map.toMutableMap()
        private val bottom = map.keys.maxOf { it.y }
        private var currentSandPoint = startSandPoint

        val restSandSet = mutableSetOf<Point>()

        fun doStep(): Boolean {
            currentSandPoint = when {
                isNotOccupied(currentSandPoint.below) -> currentSandPoint.below
                isNotOccupied(currentSandPoint.belowLeft) -> currentSandPoint.belowLeft
                isNotOccupied(currentSandPoint.belowRight) -> currentSandPoint.belowRight
                else -> {
                    restSandSet += currentSandPoint
                    map[currentSandPoint] = 'o'
                    if (currentSandPoint == startSandPoint) {
                        return false
                    }
                    startSandPoint
                }
            }
            if (hasFloor) {
                return true
            }
            return currentSandPoint.y <= bottom
        }

        private fun isNotOccupied(point: Point): Boolean {
            if (hasFloor && point.y == bottom + 2) {
                return false
            }
            return map[point] == null
        }

        fun print() {
            val points = map.keys.toMutableSet().apply {
                add(startSandPoint)
                add(currentSandPoint)
            }
            val minX = points.minOf { it.x }
            val maxX = points.maxOf { it.x }
            val minY = points.minOf { it.y }
            val maxY = if (hasFloor) bottom + 2 else points.maxOf { it.y }

            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val p = Point(x, y)
                    when {
                        p == startSandPoint -> print('+')
                        p == currentSandPoint -> print('o')
                        hasFloor && p.y == bottom + 2 -> print('#')
                        else -> print(map.getOrDefault(p, '.'))
                    }
                }
                println()
            }
            println()
        }
    }

}