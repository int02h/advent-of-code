package aoc2016

import AocDay2
import Input

class Day16 : AocDay2 {

    private lateinit var initialState: BooleanArray

    override fun readInput(input: Input) {
        initialState = input.asText().map { it == '1' }.toBooleanArray()
    }

    override fun part1() {
        calculate(272)
    }

    override fun part2() {
        calculate(35651584)
    }

    private fun calculate(diskSize: Int) {
        var state = initialState
        while (state.size < diskSize) {
            state = dragonCurve(state)
        }
        state = state.copyOfRange(0, diskSize)
        println(checksum(state).map { if (it) '1' else '0' }.joinToString(""))
    }

    private fun dragonCurve(state: BooleanArray): BooleanArray {
        return BooleanArray(2 * state.size + 1) { index ->
            when {
                index < state.size -> state[index]
                index > state.size -> !state[2 * state.size - index]
                else -> false
            }
        }
    }

    private fun checksum(state: BooleanArray): BooleanArray {
        val result = BooleanArray(state.size / 2) { index ->
            if (state[2 * index] == state[2 * index + 1]) true else false
        }
        if (result.size % 2 == 0) {
            return checksum(result)
        }
        return result
    }
}
