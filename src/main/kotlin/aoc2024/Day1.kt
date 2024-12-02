package aoc2024

import AocDay
import Input
import kotlin.math.abs

object Day1 : AocDay {
    override fun part1(input: Input) {
        val pairs = input.asLines()
            .map { line ->
                line.split("   ")
                    .map { it.trim().toInt() }
            }
        val list1 = pairs.map { it[0] }.sorted()
        val list2 = pairs.map { it[1] }.sorted()

        var result = 0
        repeat(list1.size) { index ->
            result += abs(list1[index] - list2[index])
        }
        println(result)
    }

    override fun part2(input: Input) {
        val pairs = input.asLines()
            .map { line ->
                line.split("   ")
                    .map { it.trim().toInt() }
            }
        val list1 = pairs.map { it[0] }
        val list2 = pairs.map { it[1] }

        var result = 0
        list1.forEach { item ->
            val count = list2.count { it == item }
            result += item * count
        }
        println(result)
    }
}