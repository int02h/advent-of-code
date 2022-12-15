package aoc2022

import Input
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.max

object Day15 {

    fun part1(input: Input) {
        val sensors = readSensors(input)
        val targetY = 2000000 // 10
        val line = mutableSetOf<Int>()

        sensors.forEach { s ->
            val distanceToTarget = abs(targetY - s.position.y)
            if (distanceToTarget <= s.distance) {
                val diff = s.distance - distanceToTarget
                for (x in (s.position.x - diff)..(s.position.x + diff)) {
                    line.add(x)
                }
            }
        }

        sensors.forEach { s ->
            if (s.closestBeacon.y == targetY) {
                line.remove(s.closestBeacon.x)
            }
        }

        println(line.size)
    }

    fun part2(input: Input) {
        val sensors = readSensors(input)
//        val maxDimension = 20
        val maxDimension = 4000000
        val dimensionRange = 0..maxDimension
        for (y in dimensionRange) {
            val coveredRanges = mutableListOf<IntRange>()

            sensors.forEach { s ->
                val distanceToTarget = abs(y - s.position.y)
                if (distanceToTarget <= s.distance) {
                    val diff = s.distance - distanceToTarget
                    coveredRanges += (s.position.x - diff)..(s.position.x + diff)
                }
            }

            coveredRanges.sortBy { it.first }

            var maxX = 0
            for (i in 0 until coveredRanges.lastIndex) {
                if (coveredRanges[i].first <= maxX) {
                    maxX = max(maxX, coveredRanges[i].last + 1)
                } else {
                    println("$maxX, $y")
                    println(maxX * 4000000L + y)
                    return
                }
            }
        }
    }

    private fun readSensors(input: Input): List<Sensor> {
        val matcher = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
            .matcher(input.asText())
        val sensors = mutableListOf<Sensor>()
        while (matcher.find()) {
            sensors += Sensor(
                position = Point(x = matcher.group(1).toInt(), y = matcher.group(2).toInt()),
                closestBeacon = Point(x = matcher.group(3).toInt(), y = matcher.group(4).toInt())
            )
        }
        return sensors
    }

    private data class Point(val x: Int, val y: Int) {
        fun distanceTo(other: Point): Int = abs(this.x - other.x) + abs(this.y - other.y)
    }

    private class Sensor(val position: Point, val closestBeacon: Point) {
        val distance: Int = position.distanceTo(closestBeacon)
    }

}