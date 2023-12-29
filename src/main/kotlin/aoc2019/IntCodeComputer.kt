package aoc2019

class IntCodeComputer(
    private val program: IntArray,
    private val input: MutableList<Int> = mutableListOf(),
    private val output: MutableList<Int> = mutableListOf()
) {
    var position = 0
    private val parameterModes = Array(3) { ParameterMode.PositionMode }

    fun runAll() {
        while (position != -1) {
            executeNext()
        }
    }

    fun runUntilOutput() {
        val outputTargetSize = output.size + 1
        while (position != -1 && output.size != outputTargetSize) {
            executeNext()
        }
    }

    private fun executeNext() {
        val opcode = program[position]
        getParameterModes(opcode)
        when (opcode % 100) {
            1 -> {
                val res = getValue(1) + getValue(2)
                setValue(3, res)
                position += 4
            }
            2 -> {
                val res = getValue(1) * getValue(2)
                setValue(3, res)
                position += 4
            }
            3 -> {
                setValue(1, input.removeFirst())
                position += 2
            }
            4 -> {
                output += getValue(1)
                position += 2
            }
            5 -> {
                if (getValue(1) != 0) {
                    position = getValue(2)
                } else {
                    position += 3
                }
            }
            6 -> {
                if (getValue(1) == 0) {
                    position = getValue(2)
                } else {
                    position += 3
                }
            }
            7 -> {
                val res = getValue(1) < getValue(2)
                setValue(3, if (res) 1 else 0)
                position += 4
            }
            8 -> {
                val res = getValue(1) == getValue(2)
                setValue(3, if (res) 1 else 0)
                position += 4
            }
            99 -> position = -1
            else -> error("Unknown opcode: $opcode")
        }
    }

    private fun getParameterModes(opcode: Int) {
        var value = opcode / 100
        repeat(3) {
            parameterModes[it] = ParameterMode.values()[value % 10]
            value /= 10
        }
    }

    private fun getValue(index: Int): Int =
        when (parameterModes[index - 1]) {
            ParameterMode.PositionMode -> program[program[position + index]]
            ParameterMode.ImmediateMode -> program[position + index]
        }

    private fun setValue(index: Int, value: Int) {
        when (parameterModes[index - 1]) {
            ParameterMode.PositionMode -> program[program[position + index]] = value
            ParameterMode.ImmediateMode -> error("Invalid immediate write")
        }
    }

    private enum class ParameterMode {
        PositionMode,
        ImmediateMode
    }
}