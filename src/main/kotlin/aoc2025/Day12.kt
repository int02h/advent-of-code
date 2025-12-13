package aoc2025

import AocDay2
import Input

class Day12 : AocDay2 {

    private lateinit var regions: List<Region>

    override fun readInput(input: Input) {
        val blocks = input.asText().split("\n\n")
        regions = blocks.last().split("\n")
            .map { line ->
                val (size, presents) = line.split(": ")
                val (width, height) = size.split("x").map { it.toInt() }
                Region(
                    width,
                    height,
                    presents.split(" ").map { it.toInt() }
                )
            }
    }

    override fun part1() {
        println(regions.count { it.canFit() })
    }

    override fun part2() {
    }

    private data class Region(
        val width: Int,
        val height: Int,
        val presents: List<Int>
    ) {
        fun canFit(): Boolean {
            return width * height >= presents.sumOf { it * 9 }
        }
    }
}
