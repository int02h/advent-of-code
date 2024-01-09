package aoc2019

import AocDay
import Input

object Day14 : AocDay {

    override fun part1(input: Input) {
        val reactions = parse(input)
        val chemicalRequest = mutableMapOf("FUEL" to 1)
        val queue = mutableListOf("FUEL")

        while (queue.isNotEmpty()) {
            val chemicalName = queue.removeFirst()
            val requestQuantity = chemicalRequest.getValue(chemicalName)
            if (chemicalName == "ORE") {
                continue
            }
            if (requestQuantity <= 0) {
                continue
            }
            val reaction = reactions.first { it.input.name == chemicalName }
            val multiplier = requestQuantity / reaction.input.quantity +
                    if (requestQuantity % reaction.input.quantity == 0) 0 else 1
            val leftover = multiplier * reaction.input.quantity - requestQuantity
            chemicalRequest[chemicalName] = -leftover
            reaction.outputs.forEach { output ->
                chemicalRequest[output.name] =
                    chemicalRequest.getOrDefault(output.name, 0) + multiplier * output.quantity
                queue += output.name
            }
        }

        println(chemicalRequest["ORE"])
    }

    override fun part2(input: Input) {
        val reactions = parse(input)
        val chemicalRequest = mutableMapOf<String, Long>()
        val queue = mutableListOf<String>()
        var start = 1L
        var end = 100_000_000L
        while (start <= end) {
            chemicalRequest.clear()
            chemicalRequest["FUEL"] = (start + end) / 2
            queue.clear()
            queue += "FUEL"

            while (queue.isNotEmpty()) {
                val chemicalName = queue.removeFirst()
                val requestQuantity = chemicalRequest.getValue(chemicalName)
                if (chemicalName == "ORE") {
                    continue
                }
                if (requestQuantity <= 0L) {
                    continue
                }
                val reaction = reactions.first { it.input.name == chemicalName }
                val multiplier = requestQuantity / reaction.input.quantity +
                        if (requestQuantity % reaction.input.quantity == 0L) 0L else 1L
                val leftover = multiplier * reaction.input.quantity - requestQuantity
                chemicalRequest[chemicalName] = -leftover
                reaction.outputs.forEach { output ->
                    chemicalRequest[output.name] =
                        chemicalRequest.getOrDefault(output.name, 0) + multiplier * output.quantity
                    queue += output.name
                }
            }

            val ore = chemicalRequest.getValue("ORE")
            if (ore < 1000000000000) {
                start = (start + end) / 2 + 1
            } else if (ore > 1000000000000) {
                end = (start + end) / 2 - 1
            } else {
                break
            }
        }
        println(end)
    }

    private fun parse(input: Input) =
        input.asLines().map { line ->
            val (left, right) = line.split(" => ")
                .map { it.split(", ") }
                .map { it.map(Chemical::parse) }
            Reaction(right.first(), left)
        }

    private data class Reaction(val input: Chemical, val outputs: List<Chemical>)

    private data class Chemical(val name: String, val quantity: Int) {
        companion object {
            fun parse(value: String): Chemical {
                val (quantity, name) = value.split(" ")
                return Chemical(name, quantity.toInt())
            }
        }
    }

}