package aoc2015

import AocDay
import Input

object Day20 : AocDay {

    override fun part1(input: Input) {
        val expected = input.asText().toInt()
        val max = 1_000_000
        val houses = IntArray(max)

        for (elf in 1 until max) {
            for (house in elf until max step elf) {
                houses[house] += elf * 10
            }
        }

        println(houses.indexOfFirst { it >= expected })
    }

    override fun part2(input: Input) {
        val expected = input.asText().toInt()
        val max = 1_000_000
        val houses = IntArray(max)

        for (elf in 1 until max) {
            var count = 0
            for (house in elf until max step elf) {
                houses[house] += elf * 11
                count++
                if (count == 50) break
            }
        }

        println(houses.indexOfFirst { it >= expected })
    }

}