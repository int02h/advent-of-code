package aoc2023

import AocDay
import Input
import java.util.PriorityQueue
import kotlin.math.max

object Day23 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines()
        val startPos = Position(0, 1)
        val endPos = Position(map.lastIndex, map[0].lastIndex - 1)
        val queue = mutableListOf<Entry>()
        queue.add(Entry(listOf(startPos)))

        var best = 0
        while (queue.isNotEmpty()) {
            val entry = queue.removeFirst()
            val pos = entry.path.last()
            if (entry.path.indexOf(pos) != entry.path.lastIndex) {
                continue
            }
            if (pos == endPos) {
                best = max(best, entry.path.size - 1)
                continue
            }
            when (map.get(pos)) {
                '^' -> queue.add(Entry(entry.path + pos.up))
                '>' -> queue.add(Entry(entry.path + pos.right))
                'v' -> queue.add(Entry(entry.path + pos.down))
                '<' -> queue.add(Entry(entry.path + pos.left))
                '.' -> {
                    if (map.get(pos.up) != '#') {
                        queue.add(Entry(entry.path + pos.up))
                    }
                    if (map.get(pos.down) != '#') {
                        queue.add(Entry(entry.path + pos.down))
                    }
                    if (map.get(pos.left) != '#') {
                        queue.add(Entry(entry.path + pos.left))
                    }
                    if (map.get(pos.right) != '#') {
                        queue.add(Entry(entry.path + pos.right))
                    }
                }
                else -> error("Oops")
            }
        }
        println(best)
    }

    override fun part2(input: Input) {
        val map = input.asLines()
        val startPos = Position(0, 1)
        val endPos = Position(map.lastIndex, map[0].lastIndex - 1)
        val crossroads = mutableListOf(startPos, endPos)
        for (row in 1 until map.lastIndex) {
            for (col in 1 until map[0].lastIndex) {
                val pos = Position(row, col)
                if (map.get(pos) == '#') continue
                val adjacent = listOf(pos.up, pos.down, pos.left, pos.right)
                    .filter { map.get(it) != '#' }
                if (adjacent.size > 2) {
                    crossroads += pos
                }
            }
        }

        val nodes = mutableMapOf<Position, Node>()

        for (i in crossroads.indices) {
            for (j in (i + 1)..crossroads.lastIndex) {
                val dist = bfsLong(map, crossroads, crossroads[i], crossroads[j])
                if (dist > 0) {
                    val nodeI = nodes.getOrPut(crossroads[i]) { Node(crossroads[i]) }
                    val nodeJ = nodes.getOrPut(crossroads[j]) { Node(crossroads[j]) }
                    nodeI.neighbors += nodeJ to dist
                    nodeJ.neighbors += nodeI to dist
                }
            }
        }
        println(bfs(nodes.getValue(startPos), nodes.getValue(endPos)))
    }

    private fun bfsLong(map: List<String>, crossroads: List<Position>, startPos: Position, endPos: Position): Int {
        val queue = mutableListOf<Entry>()
        queue.add(Entry(listOf(startPos)))

        var best = 0
        while (queue.isNotEmpty()) {
            val entry = queue.removeFirst()
            val pos = entry.path.last()
            if (entry.path.indexOf(pos) != entry.path.lastIndex) {
                continue
            }
            if (pos == endPos) {
                best = max(best, entry.path.size - 1)
                continue
            }
            if (pos != startPos && crossroads.contains(pos)) {
                continue
            }
            if (map.get(pos.up) != '#') {
                queue.add(Entry(entry.path + pos.up))
            }
            if (map.get(pos.down) != '#') {
                queue.add(Entry(entry.path + pos.down))
            }
            if (map.get(pos.left) != '#') {
                queue.add(Entry(entry.path + pos.left))
            }
            if (map.get(pos.right) != '#') {
                queue.add(Entry(entry.path + pos.right))
            }
        }
        return best
    }

    private fun bfs(startNode: Node, endNode: Node): Int {
        class NodeEntry(val path: List<Node>, val length: Int)

        val queue = PriorityQueue<NodeEntry> { e1, e2 -> e2.length - e1.length }
        queue.add(NodeEntry(listOf(startNode), 0))

        var best = 0
        while (queue.isNotEmpty()) {
            val entry = queue.remove()
            val node = entry.path.last()
            if (entry.path.indexOf(node) != entry.path.lastIndex) {
                continue
            }
            if (node == endNode) {
                best = max(best, entry.length)
                continue
            }
            node.neighbors.forEach {
                queue.add(NodeEntry(entry.path + it.first, entry.length + it.second))
            }
        }
        return best
    }

    private fun List<String>.get(pos: Position) = this.getOrNull(pos.row)?.getOrNull(pos.col) ?: '#'

    private data class Position(val row: Int, val col: Int) {
        val up: Position get() = copy(row = row - 1)
        val down: Position get() = copy(row = row + 1)
        val left: Position get() = copy(col = col - 1)
        val right: Position get() = copy(col = col + 1)
    }

    private class Entry(val path: List<Position>)

    private class Node(val pos: Position) {
        val neighbors = mutableListOf<Pair<Node, Int>>()

        override fun toString(): String = pos.toString()
    }
}