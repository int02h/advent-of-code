package aoc2025

import AocDay2
import Input
import util.Point3D

class Day8 : AocDay2 {

    private lateinit var boxes: List<Point3D>
    private val circuits = mutableListOf<MutableSet<Point3D>>()
    private val distances = mutableListOf<Distance>()

    override fun readInput(input: Input) {
        boxes = input.asLines()
            .map { line ->
                val (x, y, z) = line.split(",").map { it.toInt() }
                Point3D(x, y, z)
            }
        for (i in boxes.indices) {
            for (j in (i + 1) until boxes.size) {
                val value = boxes[i].distanceTo(boxes[j])
                distances += Distance(i, j, value)
            }
        }
        distances.sortBy { it.value }
    }

    // 50760
    override fun part1() {
        val directConnections = Array(boxes.size) { BooleanArray(boxes.size) }
        var connectionCount = 0
        for (d in distances) {
            if (!directConnections[d.from][d.to]) {
                addToCircuit(boxes[d.from], boxes[d.to])
                directConnections[d.from][d.to] = true
                directConnections[d.to][d.from] = true
                connectionCount++
                if (connectionCount == 1000) break
            }
        }
        println(circuits.map { it.size }.sortedDescending().distinct().take(3).fold(1) { acc, s -> acc * s })
    }

    // 3206508875
    override fun part2() {
        val directConnections = Array(boxes.size) { BooleanArray(boxes.size) }
        for (d in distances) {
            if (!directConnections[d.from][d.to]) {
                addToCircuit(boxes[d.from], boxes[d.to])
                directConnections[d.from][d.to] = true
                directConnections[d.to][d.from] = true
                if (circuits.firstOrNull()?.size == boxes.size) {
                    println(boxes[d.from].x.toLong() * boxes[d.to].x.toLong())
                    break
                }
            }
        }
    }

    private fun addToCircuit(p1: Point3D, p2: Point3D) {
        var c1: MutableSet<Point3D>? = null
        var c2: MutableSet<Point3D>? = null

        for (c in circuits) {
            if (c1 == null && c.contains(p1)) {
                c1 = c
            }
            if (c2 == null && c.contains(p2)) {
                c2 = c
            }
        }

        if (c1 == null && c2 == null) {
            circuits += mutableSetOf(p1, p2)
        } else if (c1 != null && c2 != null) {
            if (c1 !== c2) {
                c1 += c2
                circuits.remove(c2)
            }
        } else if (c1 != null) {
            c1 += p2
        } else if (c2 != null) {
            c2 += p1
        }
    }

    private class Distance(val from: Int, val to: Int, val value: Float)

}