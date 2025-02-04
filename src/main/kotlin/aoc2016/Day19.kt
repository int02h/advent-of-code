package aoc2016

import AocDay2
import Input

class Day19 : AocDay2 {

    private var elfCount: Int = 0

    override fun readInput(input: Input) {
        elfCount = input.asText().toInt()
    }

    override fun part1() {
        var (elf, _) = buildElfList()
        while (elf.nextElf !== elf) {
            elf.nextElf = elf.nextElf.nextElf
            elf = elf.nextElf
        }
        println(elf.index)
    }

    override fun part2() {
        var (elf, opposite) = buildElfList()
        while (elf.nextElf !== elf) {
            opposite.prevElf.nextElf = opposite.nextElf
            opposite.nextElf.prevElf = opposite.prevElf
            elf = elf.nextElf
            elfCount--
            opposite = if (elfCount % 2 == 0) {
                opposite.nextElf.nextElf
            } else {
                opposite.nextElf
            }
        }
        println(elf.index)
    }

    private fun buildElfList(): Pair<Elf, Elf> {
        val head = Elf(1)
        var elf = head
        val oppositeIndex = elfCount / 2 + 1
        var opposite: Elf? = null
        repeat(elfCount - 1) {
            val next = Elf(it + 2)
            if (next.index == oppositeIndex) {
                opposite = next
            }
            elf.nextElf = next
            next.prevElf = elf
            elf = next
        }
        elf.nextElf = head
        head.prevElf = elf
        return head to opposite!!
    }

    private class Elf(val index: Int) {
        lateinit var nextElf: Elf
        lateinit var prevElf: Elf
    }

}
