package aoc2015

import Input

object Day7 {

    fun part1(input: Input) {
        val gates = readGates(input)
        println(gates["a"]?.evaluate(gates))
    }

    fun part2(input: Input) {
        var gates = readGates(input)
        val aValue = gates.getValue("a").evaluate(gates)
        gates = readGates(input)
        gates["b"] = Gate(inputs = listOf(GateInput.Value(aValue)), operation = null)
        println(gates["a"]?.evaluate(gates))
    }

    private fun readGates(input: Input): MutableMap<String, Gate> {
        val map = mutableMapOf<String, Gate>()
        input.asLines()
            .map { it.split(" -> ") }
            .forEach { (left, right) ->
                val parts = left.split(' ')
                map[right] = when {
                    parts.size == 1 -> {
                        Gate(
                            inputs = listOf(readGateInput(parts.first())),
                            operation = null
                        )
                    }
                    parts.first() == "NOT" -> {
                        Gate(
                            inputs = listOf(readGateInput(parts[1])),
                            operation = Operation.NOT
                        )
                    }
                    else -> {
                        Gate(
                            inputs = listOf(readGateInput(parts[0]), readGateInput(parts[2])),
                            operation = Operation.valueOf(parts[1])
                        )
                    }
                }
            }
        return map
    }

    private fun readGateInput(value: String): GateInput =
        if (value.first().isDigit()) {
            GateInput.Value(value.toInt())
        } else {
            GateInput.Wire(value)
        }

    private class Gate(
        val inputs: List<GateInput>,
        val operation: Operation?,
    ) {

        private var evaluated: Int? = null

        fun evaluate(gates: Map<String, Gate>): Int {
            if (evaluated != null) {
                return evaluated!!
            }
            evaluated = when (operation) {
                Operation.AND -> inputs[0].evaluate(gates) and inputs[1].evaluate(gates)
                Operation.OR -> inputs[0].evaluate(gates) or inputs[1].evaluate(gates)
                Operation.NOT -> inputs[0].evaluate(gates).inv()
                Operation.LSHIFT -> inputs[0].evaluate(gates).shl(inputs[1].evaluate(gates))
                Operation.RSHIFT -> inputs[0].evaluate(gates).shr(inputs[1].evaluate(gates))
                null -> inputs.first().evaluate(gates)
            } and 0xFFFF
            return evaluated!!
        }
    }

    private sealed class GateInput {
        abstract fun evaluate(gates: Map<String, Gate>): Int

        class Value(val value: Int) : GateInput() {
            override fun evaluate(gates: Map<String, Gate>): Int = value
        }

        class Wire(val id: String) : GateInput() {
            override fun evaluate(gates: Map<String, Gate>): Int = gates.getValue(id).evaluate(gates)
        }
    }

    @Suppress("SpellCheckingInspection")
    private enum class Operation {
        AND,
        OR,
        NOT,
        LSHIFT,
        RSHIFT
    }

}