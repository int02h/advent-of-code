package aoc2023

import AocDay
import Input

object Day15 : AocDay {

    override fun part1(input: Input) {
        println(
            input.asText().split(",").sumOf(::hash)
        )
    }

    override fun part2(input: Input) {
        val steps = input.asText().split(",").map {
            if (it.endsWith('-')) {
                Step(label = it.dropLast(1), action = Action.Remove)
            } else {
                val (label, focalLength) = it.split("=")
                Step(label, Action.Add(focalLength.toInt()))
            }
        }
        val boxes = Array(256) { Box() }
        steps.forEach { s ->
            val boxIndex = hash(s.label)
            val box = boxes[boxIndex]
            when (s.action) {
                is Action.Add -> box.add(s.label, s.action.focalLength)
                Action.Remove -> box.remove(s.label)
            }
        }

        var sum = 0
        boxes.forEachIndexed { boxIndex, box ->
            box.lenses.forEachIndexed { lensIndex, lens ->
                sum += (boxIndex + 1) * (lensIndex + 1) * lens.focalLength
            }
        }
        println(sum)
    }

    private fun hash(value: String): Int {
        var result = 0
        value.forEach { ch ->
            result += ch.code
            result *= 17
            result %= 256
        }
        return result
    }

    private data class Step(val label: String, val action: Action)

    private sealed class Action {
        data class Add(val focalLength: Int) : Action()
        object Remove : Action()
    }

    private class Box {
        val lenses = mutableListOf<Lens>()

        fun add(label: String, focalLength: Int) {
            val index = lenses.indexOfFirst { it.label == label }
            if (index >= 0) {
                lenses[index] = Lens(label, focalLength)
            } else {
                lenses.add(Lens(label, focalLength))
            }
        }

        fun remove(label: String) {
            val index = lenses.indexOfFirst { it.label == label }
            if (index >= 0) {
                lenses.removeAt(index)
            }
        }
    }

    private data class Lens(val label: String, val focalLength: Int)
}