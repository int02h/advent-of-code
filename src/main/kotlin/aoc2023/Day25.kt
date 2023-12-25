package aoc2023

import AocDay
import Input
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph

object Day25 : AocDay {

    override fun part1(input: Input) {
        val graph = read(input)
        val minCut = StoerWagnerMinimumCut(graph).minCut()
        println((graph.vertexSet().size - minCut.size) * minCut.size)
    }

    override fun part2(input: Input) {
    }

    private fun read(input: Input): DefaultUndirectedGraph<String, DefaultEdge> {
        val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        input.asLines().map { line ->
            val (left, rightList) = line.split(": ")
            graph.addVertex(left)
            rightList.split(" ").forEach { right ->
                graph.addVertex(right)
                graph.addEdge(left, right)
            }
        }
        return graph
    }
}