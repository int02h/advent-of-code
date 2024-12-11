package aoc2024

import AocDay
import Input

object Day11 : AocDay {

    override fun part1(input: Input) {
        var stones = input.asText().split(" ").map { it.toLong() }
        repeat(25) {
            stones = blink(stones)
        }
        println(stones.size)
    }

    override fun part2(input: Input) {
        val stones = input.asText().split(" ").map { it.toLong() }
        var result = 0L
        val cache = mutableMapOf<String, Long>()
        stones.forEach { s ->
            result += blink(s, 1, 0, cache)
        }
        println(result)
    }

    private fun blink(stones: List<Long>): List<Long> = buildList {
        stones.forEach { s ->
            when {
                s == 0L -> add(1)
                s.toString().length % 2 == 0 -> {
                    val str = s.toString()
                    add(str.substring(0 until str.length / 2).toLong())
                    add(str.substring(str.length / 2).toLong())
                }
                else -> add(s * 2024)
            }
        }
    }

    private fun blink(
        stone: Long,
        stoneCount: Long,
        step: Int,
        cache: MutableMap<String, Long>,
    ): Long {
        if (step == 75) {
            return stoneCount
        }
        val cacheKey = "step:$step;stone:$stone"
        val cached = cache[cacheKey]
        if (cached != null) {
            return cached
        }

        val count = when {
            stone == 0L -> blink(1, stoneCount, step + 1, cache)
            stone.toString().length % 2 == 0 -> {
                val str = stone.toString()
                val part1 = blink(str.substring(0 until str.length / 2).toLong(), stoneCount, step + 1, cache)
                val part2 = blink(str.substring(str.length / 2).toLong(), stoneCount, step + 1, cache)
                part1 + part2
            }
            else -> blink(stone * 2024, stoneCount, step + 1, cache)
        }
        cache[cacheKey] = count
        return count
    }
}