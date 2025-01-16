package aoc2019

class IntCodeComputer(
    program: LongArray,
    private val input: MutableList<Long> = mutableListOf(),
    private val output: MutableList<Long> = mutableListOf()
) {
    var position = 0
    private var relativeBase = 0
    private val parameterModes = Array(3) { ParameterMode.PositionMode }

    val memory = mutableMapOf<Int, Long>()
    val overriddenWriteValues = mutableMapOf<Int, Long>()

    val isHalted: Boolean
        get() = position == -1

    init {
        program.forEachIndexed { index, value -> memory[index] = value }
    }

    fun runAll() {
        while (position != -1) {
            executeNext()
        }
    }

    fun runUntilOutput(outputCount: Int = 1) {
        val outputTargetSize = output.size + outputCount
        while (position != -1 && output.size != outputTargetSize) {
            executeNext()
        }
    }

    fun runUntilInput() {
        if (position == -1) {
            return
        }
        var opcode = getMem(position).toInt()
        while (opcode % 100 != 3) {
            executeNext()
            if (position == -1) {
                break
            }
            opcode = getMem(position).toInt()
        }
    }

    fun executeNext() {
        val opcode = getMem(position).toInt()
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
                if (getValue(1) != 0L) {
                    position = getValue(2).toInt()
                } else {
                    position += 3
                }
            }
            6 -> {
                if (getValue(1) == 0L) {
                    position = getValue(2).toInt()
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
            9 -> {
                relativeBase += getValue(1).toInt()
                position += 2
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

    private fun getValue(index: Int): Long =
        when (parameterModes[index - 1]) {
            ParameterMode.PositionMode -> getMem(getMem(position + index).toInt())
            ParameterMode.ImmediateMode -> getMem(position + index)
            ParameterMode.RelativeMode -> getMem(relativeBase + getMem(position + index).toInt())
        }

    private fun setValue(index: Int, value: Long) {
        val address = when (parameterModes[index - 1]) {
            ParameterMode.PositionMode -> getMem(position + index).toInt()
            ParameterMode.ImmediateMode -> error("Invalid immediate write")
            ParameterMode.RelativeMode -> relativeBase + getMem(position + index).toInt()
        }
        memory[address] = overriddenWriteValues.getOrDefault(address, value)
    }

    private fun getMem(position: Int): Long = memory.getOrDefault(position, 0L)

    private enum class ParameterMode {
        PositionMode,
        ImmediateMode,
        RelativeMode
    }
}