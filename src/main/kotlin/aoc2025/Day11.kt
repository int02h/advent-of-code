package aoc2025

import AocDay2
import Input
import util.Graph

class Day11 : AocDay2 {

    private val graph = Graph()
    private val cache = mutableMapOf<String, Long>()

    override fun readInput(input: Input) {
        input.asLines().forEach { line ->
            val (from, toList) = line.split(": ")
            for (to in toList.split(" ")) {
                graph.addOrientedConnection(from, to, 1)
            }
        }
    }

    override fun part1() {
        println(graph.getAllRoutes("you", "out").size)
    }

    override fun part2() {
        val svrToFft = solve2("svr", "fft")
        val fftToDac = solve2("fft", "dac")
        val dacToOut = solve2("dac", "out")

        val svrToDac = solve2("svr", "dac")
        val dacToFft = solve2("dac", "fft")
        val fftToOut = solve2("fft", "out")

        println(svrToFft * fftToDac * dacToOut + svrToDac * dacToFft * fftToOut)
    }

    private fun solve2(from: String, to: String): Long {
        if (from == to) {
            return 1
        }
        if (cache.contains("$from:$to")) {
            return cache.getValue("$from:$to")
        }
        val sum = graph.edges[from]?.keys?.sumOf { node ->
            solve2(node, to)
        } ?: 0
        cache["$from:$to"] = sum
        return sum
    }
}
