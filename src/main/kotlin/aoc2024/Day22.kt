package aoc2024

import AocDay2
import Input

class Day22 : AocDay2 {

    private lateinit var secrets: LongArray

    override fun readInput(input: Input) {
        secrets = input.asLines().map { it.toLong() }.toLongArray()
    }

    override fun part1() {
        repeat(2000) {
            for (i in secrets.indices) {
                secrets[i] = next(secrets[i])
            }
        }
        println(secrets.sum())
    }

    override fun part2() {
        val prices = Array(secrets.size) { mutableListOf((secrets[it] % 10).toInt()) }
        val diffs = Array(secrets.size) { mutableListOf(0) }

        repeat(2000) {
            for (i in secrets.indices) {
                secrets[i] = next(secrets[i])
                val price = (secrets[i] % 10).toInt()
                val lastPrice = prices[i].lastOrNull()
                prices[i] += price
                if (lastPrice != null) {
                    diffs[i] += price - lastPrice
                }
            }
        }

        val map = mutableMapOf<List<Int>, Int>().withDefault { 0 }
        for (di in diffs.indices) {
            val seen = mutableSetOf<List<Int>>()
            for (i in 3..diffs[di].lastIndex) {
                val sequence = listOf(diffs[di][i - 3], diffs[di][i - 2], diffs[di][i - 1], diffs[di][i])
                if (!seen.contains(sequence)) {
                    map[sequence] = map.getValue(sequence) + prices[di][i]
                    seen += sequence
                }
            }
        }
        println(map.values.max())
    }

    private fun next(secret: Long): Long {
        val x64 = secret * 64L
        var next = (secret xor x64) % 16777216L

        val d32 = next / 32L
        next = (next xor d32) % 16777216L

        return (next * 2048 xor next) % 16777216L
    }

}
