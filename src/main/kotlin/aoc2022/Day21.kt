package aoc2022

import Input

object Day21 {

    fun part1(input: Input) {
        val monkeys = readMonkey(input)
        println(monkeys.getValue("root").evaluate(monkeys))
    }

    fun part2(input: Input) {
        val monkeys = readMonkey(input)
        val oldRoot = monkeys.getValue("root") as Monkey.OperationMonkey
        val newRoot = Monkey.OperationMonkey(oldRoot.name, oldRoot.monkey1, oldRoot.monkey2, '-')
        monkeys["root"] = newRoot

        monkeys["humn"] = object : Monkey() {
            override val name: String = "humn"

            override fun evaluate(monkeys: Map<String, Monkey>): Long {
                throw HumanException()
            }

            override fun evaluateWithExpected(monkeys: Map<String, Monkey>, expected: Long) {
                println(expected)
            }

        }

        newRoot.evaluateWithExpected(monkeys, 0)
    }

    private fun readMonkey(input: Input): MutableMap<String, Monkey> {
        val monkeys = mutableMapOf<String, Monkey>()
        input.asLines()
            .map { line -> line.split(": ") }
            .forEach { (name, yelling) ->
                val parts = yelling.split(' ')
                val monkey = if (parts.size == 1) {
                    Monkey.NumberMonkey(name, parts.first().toLong())
                } else {
                    Monkey.OperationMonkey(name, parts[0], parts[2], parts[1].first())
                }
                monkeys[name] = monkey
            }
        return monkeys
    }

    private abstract class Monkey {

        abstract val name: String

        abstract fun evaluate(monkeys: Map<String, Monkey>): Long
        abstract fun evaluateWithExpected(monkeys: Map<String, Monkey>, expected: Long)

        class NumberMonkey(
            override val name: String,
            val value: Long
        ) : Monkey() {

            override fun evaluate(monkeys: Map<String, Monkey>): Long = value

            override fun evaluateWithExpected(monkeys: Map<String, Monkey>, expected: Long) {
                if (expected != value) {
                    error("Unexpected value for $name")
                }
            }

        }

        class OperationMonkey(
            override val name: String,
            val monkey1: String,
            val monkey2: String,
            val operation: Char
        ) : Monkey() {

            override fun evaluate(monkeys: Map<String, Monkey>): Long {
                val monkey1 = monkeys.getValue(monkey1)
                val monkey2 = monkeys.getValue(monkey2)
                return when (operation) {
                    '+' -> monkey1.evaluate(monkeys) + monkey2.evaluate(monkeys)
                    '-' -> monkey1.evaluate(monkeys) - monkey2.evaluate(monkeys)
                    '*' -> monkey1.evaluate(monkeys) * monkey2.evaluate(monkeys)
                    '/' -> monkey1.evaluate(monkeys) / monkey2.evaluate(monkeys)
                    else -> error(operation)
                }
            }

            override fun evaluateWithExpected(monkeys: Map<String, Monkey>, expected: Long) {
                val monkey1 = monkeys.getValue(monkey1)
                val monkey2 = monkeys.getValue(monkey2)

                var test = 0
                try {
                    monkey1.evaluate(monkeys)
                } catch (_: HumanException) {
                    test++
                }
                try {
                    monkey2.evaluate(monkeys)
                } catch (_: HumanException) {
                    test++
                }
                if (test == 2) {
                    error("Shiiit")
                }

                when (operation) {
                    '+' -> {
                        try {
                            monkey1.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey1.evaluateWithExpected(monkeys, expected - monkey2.evaluate(monkeys))
                        }
                        try {
                            monkey2.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey2.evaluateWithExpected(monkeys, expected - monkey1.evaluate(monkeys))
                        }
                    }
                    '-' -> {
                        try {
                            monkey1.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey1.evaluateWithExpected(monkeys, expected + monkey2.evaluate(monkeys))
                        }
                        try {
                            monkey2.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey2.evaluateWithExpected(monkeys, -expected + monkey1.evaluate(monkeys))
                        }
                    }
                    '*' -> {
                        try {
                            monkey1.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey1.evaluateWithExpected(monkeys, expected / monkey2.evaluate(monkeys))
                        }
                        try {
                            monkey2.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey2.evaluateWithExpected(monkeys, expected / monkey1.evaluate(monkeys))
                        }
                    }
                    '/' -> {
                        try {
                            monkey1.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey1.evaluateWithExpected(monkeys, expected * monkey2.evaluate(monkeys))
                        }
                        try {
                            monkey2.evaluate(monkeys)
                        } catch (e: HumanException) {
                            monkey2.evaluateWithExpected(monkeys, monkey1.evaluate(monkeys) / expected)
                        }
                    }
                    else -> error(operation)
                }
            }

        }
    }

    private class HumanException : Exception()

}