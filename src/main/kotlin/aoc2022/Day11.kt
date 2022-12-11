package aoc2022

import Input

object Day11 {

    fun part1(input: Input) {
        val monkeys = readMonkeys(input)
        repeat(20) {
            monkeys.forEach { m -> m.doTurn(monkeys) { it / 3 } }
        }

        val sorted = monkeys.sortedByDescending { it.inspectCount }
        println(sorted[0].inspectCount * sorted[1].inspectCount)
    }

    fun part2(input: Input) {
        val monkeys = readMonkeys(input)
        val lcm = monkeys.map { it.test.testValue }
            .distinct()
            .fold(1) { acc, value -> acc * value }
        repeat(10000) {
            monkeys.forEach { m -> m.doTurn(monkeys) { it % lcm } }
        }

        val sorted = monkeys.sortedByDescending { it.inspectCount }
        println(sorted[0].inspectCount.toLong() * sorted[1].inspectCount.toLong())
    }

    private fun readMonkeys(input: Input): List<Monkey> {
        val lines = input.asLines().filter { it.trim().isNotEmpty() }
        val monkeyCount = lines.size / 6
        return (0 until monkeyCount).map { index ->
            val items = lines[6 * index + 1].split(':')[1].trim().split(", ").map { it.toLong() }
            val operation = parseOperation(lines[6 * index + 2].split(':')[1].trim())
            val test = parseTest(lines[6 * index + 3], lines[6 * index + 4], lines[6 * index + 5])
            Monkey(items, operation, test)
        }
    }

    private fun parseOperation(value: String): Operation {
        val (op1, operation, op2) = value.split(' ').drop(2)
        return if (operation == "+") {
            Operation.Plus(parseOperand(op1), parseOperand(op2))
        } else {
            Operation.Mul(parseOperand(op1), parseOperand(op2))
        }
    }

    private fun parseOperand(value: String): Operand =
        if (value == "old") Operand.Old else Operand.Literal(value.toLong())

    private fun parseTest(testValue: String, trueValue: String, falseValue: String) =
        Test(
            testValue = testValue.substringAfterLast(' ').toInt(),
            trueValue = trueValue.substringAfterLast(' ').toInt(),
            falseValue = falseValue.substringAfterLast(' ').toInt(),
        )

    class Monkey(
        items: List<Long>,
        private val operation: Operation,
        val test: Test
    ) {

        private var items = items.toMutableList()
        var inspectCount = 0

        fun doTurn(monkeys: List<Monkey>, relief: (Long) -> Long) {
            while (items.isNotEmpty()) {
                val oldItem = items.removeAt(0)
                val newItem = relief(operation.evaluate(oldItem))
                val monkeyIndex = if (newItem % test.testValue == 0L) test.trueValue else test.falseValue
                monkeys[monkeyIndex].items += newItem
                inspectCount++
            }
        }

    }

    sealed class Operation {
        abstract fun evaluate(value: Long): Long

        class Plus(private val op1: Operand, private val op2: Operand) : Operation() {
            override fun evaluate(value: Long): Long = op1.evaluate(value) + op2.evaluate(value)
        }

        class Mul(private val op1: Operand, private val op2: Operand) : Operation() {
            override fun evaluate(value: Long): Long = op1.evaluate(value) * op2.evaluate(value)
        }
    }

    sealed class Operand {
        abstract fun evaluate(value: Long): Long

        object Old : Operand() {
            override fun evaluate(value: Long): Long = value
        }

        class Literal(private val value: Long) : Operand() {
            override fun evaluate(value: Long): Long = this.value
        }
    }

    class Test(
        val testValue: Int,
        val trueValue: Int,
        val falseValue: Int
    )

}