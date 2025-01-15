package aoc2024

import AocDay2
import Input

class Day25 : AocDay2 {

    private val keys = mutableListOf<IntArray>()
    private val locks = mutableListOf<IntArray>()
    private val size = 5

    override fun readInput(input: Input) {
        val blocks = input.asText().split("\n\n").map { it.split("\n") }
        blocks.forEach { b ->
            val heights = IntArray(b.first().length) { -1 }
            b.forEach { line ->
                line.forEachIndexed { index, ch ->
                    if (ch == '#') heights[index]++
                }
            }
            if (b.first().all { it == '.' }) {
                keys += heights
            } else {
                locks += heights
            }
        }
    }

    override fun part1() {
        var count = 0
        keys.forEach { key ->
            locks.forEach { lock ->
                if (checkFit(key = key, lock = lock)) count++
            }
        }
        println(count)
    }

    override fun part2() {
    }

    private fun checkFit(key: IntArray, lock: IntArray): Boolean {
        for (i in 0 until size) {
            if (key[i] + lock[i] > size) return false
        }
        return true
    }
}