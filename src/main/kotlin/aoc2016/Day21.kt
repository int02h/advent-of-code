package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day21 : AocDay2 {

    private lateinit var operations: List<Operation>

    override fun readInput(input: Input) {
        operations = input.asLines().map { line ->
            val values = line.findAllNumbers()
            when {
                line.startsWith("swap position") -> Operation.SwapPosition(values[0], values[1])
                line.startsWith("swap letter") -> Operation.SwapLetter(line[12], line.last())
                line.startsWith("rotate left") -> Operation.Rotate(-values[0])
                line.startsWith("rotate right") -> Operation.Rotate(values[0])
                line.startsWith("rotate based") -> Operation.RotateBased(line.last())
                line.startsWith("reverse positions") -> Operation.ReversePositions(values[0], values[1])
                line.startsWith("move position") -> Operation.MovePosition(values[0], values[1])
                else -> error(line)
            }
        }
    }

    override fun part1() {
        val chars = "abcdefgh".toCharArray()
        operations.forEach { it.perform(chars) }
        println(String(chars))
    }

    override fun part2() {
        operations = operations.map { op ->
            when (op) {
                is Operation.MovePosition -> Operation.MovePosition(from = op.to, to = op.from)
                is Operation.ReversePositions -> op
                is Operation.Rotate -> Operation.Rotate(-op.steps)
                is Operation.RotateBased -> Operation.RotateBasedInv(op)
                is Operation.SwapLetter -> op
                is Operation.SwapPosition -> op
                is Operation.RotateBasedInv -> error("Impossible")
            }
        }

        val chars = "fbgdceah".toCharArray()
        operations.reversed().forEach { it.perform(chars) }
        println(String(chars))
    }

    private sealed interface Operation {
        fun perform(value: CharArray)

        class SwapPosition(private val pos1: Int, private val pos2: Int) : Operation {
            override fun perform(value: CharArray) {
                val tmp = value[pos1]
                value[pos1] = value[pos2]
                value[pos2] = tmp
            }
        }

        class SwapLetter(val l1: Char, val l2: Char) : Operation {
            override fun perform(value: CharArray) {
                for (i in value.indices) {
                    if (value[i] == l1) value[i] = l2
                    else if (value[i] == l2) value[i] = l1
                }
            }
        }

        class Rotate(val steps: Int) : Operation {
            override fun perform(value: CharArray) {
                value.shift(steps)
            }
        }

        class RotateBased(val letter: Char) : Operation {
            override fun perform(value: CharArray) {
                val index = value.indexOf(letter)
                val steps = 1 + index + (if (index >= 4) 1 else 0)
                value.shift(steps)
            }
        }

        class RotateBasedInv(val op: RotateBased) : Operation {
            override fun perform(value: CharArray) {
                val original = value.copyOf()
                do {
                    value.shift(-1)
                    val res = value.copyOf()
                    op.perform(res)
                } while (!res.contentEquals(original))
            }
        }

        class ReversePositions(val from: Int, val to: Int) : Operation {
            override fun perform(value: CharArray) {
                val copy = value.copyOfRange(from, to + 1)
                for (i in from..to) {
                    value[i] = copy[copy.lastIndex + from - i]
                }
            }
        }

        class MovePosition(val from: Int, val to: Int) : Operation {
            override fun perform(value: CharArray) {
                if (from == to) return
                val copy = value.copyOf()
                var vi = 0
                var ci = 0
                val buf = copy[from]
                while (true) {
                    if (ci == from) {
                        ci++
                    } else if (vi == to) {
                        value[vi] = buf
                        vi++
                    }
                    if (ci == copy.size || vi == value.size) break
                    value[vi] = copy[ci]
                    ci++
                    vi++
                }
            }
        }

        fun CharArray.shift(steps: Int) {
            val copy = copyOf()
            for (i in indices) {
                var j = i + steps
                while (j < 0) j += size
                while (j >= size) j -= size
                this[j] = copy[i]
            }
        }
    }

}
