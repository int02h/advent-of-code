package aoc2016

import AocDay2
import Input
import util.XYPosition as Position
import util.findAllNumbers
import kotlin.math.abs

class Day22 : AocDay2 {

    private val nodes = mutableMapOf<Position, Node>().withDefault { Node(0, 0) }

    override fun readInput(input: Input) {
        input.asLines().drop(2).map { line ->
            val (x, y, size, used) = line.findAllNumbers()
            nodes[Position(x, y)] = Node(used, size)
        }
    }

    override fun part1() {
        var count = 0
        val nodes = this.nodes.values.toList()
        for (i in nodes.indices) {
            for (j in (i + 1)..nodes.lastIndex) {
                if (
                    (nodes[i].used > 0 && nodes[i].used < nodes[j].available) ||
                    (nodes[j].used > 0 && nodes[j].used < nodes[i].available)
                ) {
                    count++
                }
            }
        }
        println(count)
    }

    override fun part2() {
        val maxX = nodes.keys.maxOf { it.x }
        val holeY = nodes.filterValues { it.used > 100 }.keys.first().y
        val holeX = nodes.filter { it.key.y == holeY && it.value.used < 100 }.keys.first().x
        val (emptyX, emptyY) = nodes.filterValues { it.used == 0 }.keys.first()
        val distanceToHole = abs(emptyX - holeX) + abs(emptyY - holeY)
        val distanceToData = abs(holeX - (maxX - 1)) + abs(holeY - 1)
        println(distanceToHole + distanceToData + (maxX - 1) * 5 + 2)
    }

    private data class Node(val used: Int, val size: Int, val target: Boolean = false) {
        val available: Int = size - used
    }

}
