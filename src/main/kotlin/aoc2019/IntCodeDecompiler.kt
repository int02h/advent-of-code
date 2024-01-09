package aoc2019

class IntCodeDecompiler(private val program: LongArray) {
    private var position = 0
    private val parameterModes = Array(3) { ParameterMode.PositionMode }

    fun decompile(): String {
        val builder = StringBuilder()
        while (position < program.size) {
            decompileOpcodeAtPosition(builder)
            builder.append("\n")
        }
        return builder.toString()
    }

    private fun decompileOpcodeAtPosition(builder: StringBuilder) {
        val opcode = program[position].toInt()
        getParameterModes(opcode)
        builder.append(position.toString().padStart(4)).append(": ")
        when (opcode % 100) {
            1 -> {
                builder.append("${decompileValue(3)} = ${decompileValue(1)} + ${decompileValue(2)}")
                position += 4
            }
            2 -> {
                builder.append("${decompileValue(3)} = ${decompileValue(1)} * ${decompileValue(2)}")
                position += 4
            }
            3 -> {
                builder.append("${decompileValue(1)} = IN")
                position += 2
            }
            4 -> {
                builder.append("OUT = ${decompileValue(1)}")
                position += 2
            }
            5 -> {
                builder.append("IF ${decompileValue(1)} != 0 JMP ${decompileValue(2)}")
                position += 3
            }
            6 -> {
                builder.append("IF ${decompileValue(1)} == 0 JMP ${decompileValue(2)}")
                position += 3
            }
            7 -> {
                builder.append("${decompileValue(3)} = ${decompileValue(1)} < ${decompileValue(2)}")
                position += 4
            }
            8 -> {
                builder.append("${decompileValue(3)} = ${decompileValue(1)} == ${decompileValue(2)}")
                position += 4
            }
            9 -> {
                builder.append("RB = ${decompileValue(1)}")
                position += 2
            }
            99 -> {
                builder.append("HALT")
                position += 1
            }
            else -> {
                builder.append(opcode)
                position += 1
            }
        }
    }

    private fun decompileValue(index: Int): String =
        when (parameterModes[index - 1]) {
            ParameterMode.PositionMode -> "mem[${program[position + index]}]"
            ParameterMode.ImmediateMode -> program[position + index].toString()
            ParameterMode.RelativeMode -> "RB+${program[position + index]}"
        }

    private fun getParameterModes(opcode: Int) {
        var value = opcode / 100
        repeat(3) {
            parameterModes[it] = ParameterMode.values()[value % 10]
            value /= 10
        }
    }

    private enum class ParameterMode {
        PositionMode,
        ImmediateMode,
        RelativeMode
    }
}