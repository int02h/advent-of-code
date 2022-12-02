package aoc2022

import Input

object Day1 {
    fun part1(input: Input) {
        val elfList = readElfList(input)
        println(
            elfList.maxOfOrNull { it.foodCalories.sum() }
        )
    }

    fun part2(input: Input) {
        val elfList = readElfList(input)
        elfList.sortByDescending { it.foodCalories.sum() }
        println(
            elfList.take(3).sumOf { it.foodCalories.sum() }
        )
    }

    private fun readElfList(input: Input): MutableList<Elf> {
        val elfList = mutableListOf(Elf())
        input.asLines()
            .forEach { line ->
                if (line.isEmpty()) {
                    elfList += Elf()
                } else {
                    elfList.last().foodCalories += line.toInt()
                }
            }
        return elfList
    }

    class Elf {
        val foodCalories = mutableListOf<Int>()
    }
}