package aoc2022

import Input

object Day3 {
    fun part1(input: Input) {
        input.asLines()
            .map { Rucksack(it) }
            .sumOf { r -> r.common.sumOf { ch -> ch.getScore() } }
            .also(::println)
    }

    fun part2(input: Input) {
        val rucksacks = input.asLines().map { Rucksack(it) }
        (1..(rucksacks.size / 3))
            .map { index -> rucksacks.subList((index - 1) * 3, index * 3) }
            .map { (r1, r2, r3) ->
                r1.value.toSet().intersect(r2.value.toSet()).intersect(r3.value.toSet())
            }
            .sumOf { c -> c.sumOf { ch -> ch.getScore() } }
            .also(::println)
    }

    private class Rucksack(val value: String) {
        val compartment1: String = value.substring(0, value.length / 2)
        val compartment2: String = value.substring(value.length / 2)
        val common = compartment1.toSet().intersect(compartment2.toSet())
    }

    private fun Char.getScore(): Int =
        when (this) {
            in 'a'..'z' -> this - 'a' + 1
            in 'A'..'Z' -> this - 'A' + 27
            else -> error("bad char $this")
        }
}
