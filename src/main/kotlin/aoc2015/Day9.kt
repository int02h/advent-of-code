package aoc2015

import Input
import util.Graph

object Day9 {

    fun part1(input: Input) {
        println(getAllRoutes(input).minOf { it.totalDistance })
    }

    fun part2(input: Input) {
        println(getAllRoutes(input).maxOf { it.totalDistance })
    }

    private fun getAllRoutes(input: Input): List<Graph.Route> {
        val graph = Graph()
        input.asLines()
            .map { line -> line.split(' ') }
            .map { values ->
                val from = values[0]
                val to = values[2]
                val distance = values[4].toInt()
                graph.addConnection(from, to, distance)
            }
        val routes = mutableListOf<Graph.Route>()
        graph.nodes.forEach { node ->
            graph.getRoutes(node, graph.nodes.size, routes)
        }
        return routes
    }

}