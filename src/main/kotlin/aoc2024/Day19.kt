package aoc2024

import AocDay2
import Input

class Day19 : AocDay2 {

    private lateinit var towels: List<String>
    private lateinit var designs: List<String>

    private val cache = mutableMapOf<String, Long>()

    override fun readInput(input: Input) {
        val lines = input.asLines()
        designs = lines.drop(2)
        towels = lines[0].split(", ")
    }

    override fun part1() {
        println(designs.count { d -> backtrack(d, emptyList()) })
    }

    override fun part2() {
        val towelsBackup = ArrayList(towels)
        designs.forEach { d ->
            towels = towels.filter { t -> d.contains(t) }
            var designPart = ""
            d.reversed().forEach { ch ->
                designPart = ch + designPart
                val count = backtrack2(designPart, emptyList())
                cache[designPart] = count
            }
            towels = towelsBackup
        }
        println(designs.sumOf { cache.getValue(it) })
    }

    private fun backtrack(targetDesign: String, towelIndices: List<Int>): Boolean {
        val currentDesign = buildDesign(towelIndices)
        if (!targetDesign.startsWith(currentDesign)) {
            return false
        }
        if (targetDesign == currentDesign) {
            return true
        }
        var indices = towelIndices + 0
        while (indices.isNotEmpty()) {
            if (backtrack(targetDesign, indices)) {
                return true
            }
            indices = nextIndices(indices)
        }
        return false
    }

    private fun backtrack2(targetDesign: String, towelIndices: List<Int>): Long {
        val currentDesign = buildDesign(towelIndices)
        if (!targetDesign.startsWith(currentDesign)) {
            return 0
        }
        if (targetDesign == currentDesign) {
            return 1
        }
        val restDesign = targetDesign.substring(currentDesign.length)
        if (cache.containsKey(restDesign)) {
            return cache.getValue(restDesign)
        }
        var indices = towelIndices + 0
        var sum = 0L
        while (indices.isNotEmpty()) {
            sum += backtrack2(targetDesign, indices)
            indices = nextIndices(indices)
        }
        return sum
    }

    private fun buildDesign(towelIndices: List<Int>): String = buildString {
        towelIndices.forEach { ti -> append(towels[ti]) }
    }

    private fun nextIndices(towelIndices: List<Int>): List<Int> {
        val result = ArrayList(towelIndices)
        var i = towelIndices.lastIndex
        result[i]++
        while (result[i] == towels.size) {
            result[i] = 0
            i--
            if (i >= 0) {
                result[i]++
            } else {
                return emptyList()
            }
        }
        return result.dropLastWhile { it == 0 }
    }
}