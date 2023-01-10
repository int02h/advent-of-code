package aoc2015

import AocDay
import Input

object Day16 : AocDay {

    private val expectedProps = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    override fun part1(input: Input) {
        val aunts = readAunts(input)
        val filtered = aunts.filter { aunt ->
            aunt.properties.all { (name, value) ->
                expectedProps[name] == value
            }
        }
        println(filtered.first().name)
    }

    override fun part2(input: Input) {
        val aunts = readAunts(input)
        val filtered = aunts.filter { aunt ->
            aunt.properties.all { (name, value) ->
                when (name) {
                    "cats", "trees" -> value > expectedProps.getValue(name)
                    "pomeranians", "goldfish" -> value < expectedProps.getValue(name)
                    else -> value == expectedProps.getValue(name)
                }
            }
        }
        println(filtered.first().name)
    }

    private fun readAunts(input: Input): List<Aunt> =
        input.asLines()
            .map {
                val index = it.indexOf(":")
                it.substring(0, index).trim() to it.substring(index + 1).trim()
            }
            .map { (name, props) -> name to props.split(", ") }
            .map { (name, props) ->
                Aunt(name).apply {
                    props.map { it.split(": ") }
                        .forEach { (name, value) -> setProperty(name, value.toInt()) }
                }
            }

    private class Aunt(val name: String) {
        val properties = mutableMapOf<String, Int>()

        fun setProperty(name: String, value: Int) {
            properties[name] = value
        }

    }
}