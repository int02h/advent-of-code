package aoc2022

import Input

object Day20 {

    fun part1(input: Input) {
        val original = input.asLines().map { Number(it.toLong()) }
        val numbers = original.toMutableList()

        original.forEach { originalNumber ->
            val startIndex = numbers.indexOf(originalNumber)
            val endIndex = startIndex + originalNumber.value
            numbers.removeAt(startIndex)
            numbers.addWithWrappingAt(endIndex, originalNumber)
        }

        val indexOfZero = numbers.indexOfFirst { it.value == 0L }
        println(
            numbers.getWithWrappingAt(indexOfZero + 1000).value +
                    numbers.getWithWrappingAt(indexOfZero + 2000).value +
                    numbers.getWithWrappingAt(indexOfZero + 3000).value
        )
    }

    fun part2(input: Input) {
        val original = input.asLines().map { Number(811589153L * it.toLong()) }
        val numbers = original.toMutableList()

        repeat(10) {
            original.forEach { originalNumber ->
                val startIndex = numbers.indexOf(originalNumber)
                val endIndex = startIndex + originalNumber.value
                numbers.removeAt(startIndex)
                numbers.addWithWrappingAt(endIndex, originalNumber)
            }
            println("Iteration $it")
        }

        val indexOfZero = numbers.indexOfFirst { it.value == 0L }
        println(
            numbers.getWithWrappingAt(indexOfZero + 1000).value +
                    numbers.getWithWrappingAt(indexOfZero + 2000).value +
                    numbers.getWithWrappingAt(indexOfZero + 3000).value
        )

    }

    private fun MutableList<Number>.addWithWrappingAt(index: Long, number: Number) {
        var wrappedIndex = index
        if (wrappedIndex < 0) {
            val count = -wrappedIndex / size
            wrappedIndex += (count + 1) * size
        }
        wrappedIndex %= size
        add(wrappedIndex.toInt(), number)
    }

    private fun MutableList<Number>.getWithWrappingAt(index: Int): Number {
        var wrappedIndex = index
        while (wrappedIndex < 0) {
            wrappedIndex += size
        }
        wrappedIndex %= size
        return this[wrappedIndex]
    }

    private class Number(val value: Long)
}
