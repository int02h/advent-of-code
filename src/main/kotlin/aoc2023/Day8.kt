package aoc2023

import AocDay
import Input
import java.util.regex.Pattern

object Day8 : AocDay {

    override fun part1(input: Input) {
        val (instructions, nodes) = parse(input)
        var node = nodes.getValue("AAA")
        val finish = nodes.getValue("ZZZ")

        var i = 0
        var count = 0
        while (node !== finish) {
            when (instructions[i]) {
                'R' -> node = node.right
                'L' -> node = node.left
            }
            count++
            i = (i + 1) % instructions.length
        }
        println(count)
    }

    override fun part2(input: Input) {
        val (instructions, allNodes) = parse(input)
        val nodes = allNodes.values.filter { it.name.endsWith('A') }.toMutableList()
        val nodeCount = LongArray(nodes.size)

        nodes.forEachIndexed { index, it ->
            var i = 0
            var node = it
            var count = 0L
            while (!node.name.endsWith('Z')) {
                when (instructions[i]) {
                    'R' -> node = node.right
                    'L' -> node = node.left
                }
                count++
                i = (i + 1) % instructions.length
            }
            nodeCount[index] = count
        }

        println(
            nodeCount.fold(1L) { acc, count -> lcm(acc, count) }
        )
    }

    private fun parse(input: Input): Pair<String, Map<String, Node>> {
        val pattern = Pattern.compile("([0-9A-Z]+) = \\(([0-9A-Z]+), ([0-9A-Z]+)\\)")
        val lines = input.asLines()
        val nodeMap = mutableMapOf<String, Node>()
        (2..lines.lastIndex).map {
            with(pattern.matcher(lines[it])) {
                find()
                val name = this.group(1)
                val left = this.group(2)
                val right = this.group(3)
                val node = nodeMap.getOrPut(name) { Node(name) }
                node.left = nodeMap.getOrPut(left) { Node(left) }
                node.right = nodeMap.getOrPut(right) { Node(right) }
                node
            }
        }
        return lines.first() to nodeMap
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long {
        return (a * b) / gcd(a, b)
    }

    class Node(val name: String) {
        lateinit var left: Node
        lateinit var right: Node
    }

}