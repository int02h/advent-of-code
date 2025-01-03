package aoc2015

import AocDay2
import Input
import kotlin.math.min

class Day24 : AocDay2 {

    private lateinit var weights: List<Int>

    override fun readInput(input: Input) {
        weights = input.asLines().map { it.toInt() }
    }

    override fun part1() {
        exec(3)
    }

    override fun part2() {
        exec(4)
    }

    private fun exec(groupCount: Int) {
        data class State(val group1: Set<Int>, val left: List<Int>)

        val allSum = weights.sum()
        val cache = mutableMapOf<State, Set<Int>?>()
        var currentBestSize = weights.size / groupCount

        fun dp(state: State): Set<Int>? {
            if (cache.containsKey(state)) {
                return cache[state]
            }
            if (state.group1.sum() == allSum / groupCount) {
                currentBestSize = min(currentBestSize, state.group1.size)
                return state.group1
            }
            if (state.group1.size >= currentBestSize) {
                return null
            }
            if (state.group1.sum() > allSum / groupCount) {
                return null
            }
            if (state.left.isEmpty()) {
                return null
            }

            val next = state.left.first()
            val a = dp(State(state.group1 + next, state.left - next))
            val b = dp(State(state.group1, state.left - next))
            if (a == null && b == null) {
                return null
            }

            val aSize = a?.size ?: Int.MAX_VALUE
            val bSize = b?.size ?: Int.MAX_VALUE
            val bestSet = if (aSize == bSize) {
                val aQE = a?.quantumEntanglement() ?: Long.MAX_VALUE
                val bQE = b?.quantumEntanglement() ?: Long.MAX_VALUE
                if (aQE < bQE) a else b
            } else if (aSize < bSize) a else b
            cache[state] = bestSet
            return bestSet
        }

        val best = dp(State(emptySet(), weights.sortedDescending()))
        println(best!!.quantumEntanglement())
    }

    private fun Set<Int>.quantumEntanglement(): Long = fold(1L) { acc, w -> acc * w }
}