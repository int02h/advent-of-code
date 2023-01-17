package aoc2015

import AocDay
import Input

object Day19 : AocDay {

    override fun part1(input: Input) {
        val (molecule, replacements) = parseInput(input)
        val result = mutableSetOf<String>()

        replacements.forEach { (key, value) ->
            var index = molecule.indexOf(key)
            while (index >= 0) {
                result += molecule.replaceRange(index, index + key.length, value)
                index = molecule.indexOf(key, startIndex = index + 1)
            }
        }

        println(result.size)
    }

    override fun part2(input: Input) {
        val (originalMolecule, replacements) = parseInput(input, reversed = true)
        var molecule = originalMolecule
        var count = 0

        while (molecule != "e") {
            var found = false
            for (r in replacements) {
                val indexes = molecule.getAllIndexes(r.first)
                if (indexes.isNotEmpty()) {
                    val index = indexes.random()
                    molecule = molecule.replaceRange(index, index + r.first.length, r.second)
                    found = true
                    count++
                }
            }
            if (!found) {
                count = 0
                molecule = originalMolecule
            }
        }

        println(count)
    }

    private fun String.getAllIndexes(substring: String): List<Int> {
        var index = indexOf(substring)
        val result = mutableListOf<Int>()
        while (index >= 0) {
            result += index
            index = indexOf(substring, index + substring.length)
        }
        return result
    }

    private fun parseInput(input: Input, reversed: Boolean = false): Pair<String, List<Pair<String, String>>> {
        val (replacements, molecule) = input.asText().split("\n\n").map { it.trim() }
        return molecule to replacements.split("\n")
            .map { it.split(" => ") }
            .map { (key, value) -> if (reversed) value to key else key to value }

    }

}