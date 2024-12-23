package aoc2024

import AocDay2
import Input

class Day23 : AocDay2 {

    private lateinit var computers: List<Computer>

    override fun readInput(input: Input) {
        val computers = mutableMapOf<String, Computer>()
        input.asLines()
            .map {
                val (from, to) = it.split("-")
                computers.getOrPut(from) { Computer(from) }.connectWith(computers.getOrPut(to) { Computer(to) })
            }
        this.computers = computers.values.toList()
    }

    override fun part1() {
        val groupCount = mutableMapOf<Set<Computer>, Int>().withDefault { 0 }
        for (comp in computers) {
            for (i in 0..(comp.connected.size - 2)) {
                for (j in (i + 1)..(comp.connected.size - 1)) {
                    val group = setOf(comp, comp.connected[i], comp.connected[j])
                    groupCount[group] = groupCount.getValue(group) + 1
                }
            }
        }

        println(
            groupCount.count { (group, count) -> count == 3 && group.any { it.name.startsWith('t') } }
        )
    }

    override fun part2() {
        val allLoops = mutableMapOf<Computer, List<List<Computer>>>()
        computers.forEach { c ->
            allLoops[c] = c.findLoops()
        }

        val allGroups = mutableMapOf<Set<Computer>, Int>()
        computers.forEach { c ->
            val loops = allLoops.getValue(c)
            val group = loops.flatten().toSet()
            allGroups[group] = allGroups.getOrDefault(group, 0) + 1
        }

        val validGroups = allGroups.filter { (group, count) -> group.size == count }
        if (validGroups.size != 1) error("Oops")
        println(
            validGroups.keys.first().map { it.name }.sorted().joinToString(",")
        )
    }

    private data class Computer(val name: String) {
        val connected = mutableListOf<Computer>()

        fun connectWith(computer: Computer) {
            connected += computer
            computer.connected += this
        }

        fun findLoops(): List<List<Computer>> {
            class QueueItem(val computer: Computer, val loop: List<Computer>)

            val queue = mutableListOf(QueueItem(this, listOf(this)))
            val visited = mutableSetOf<String>()
            val loops = mutableListOf<List<Computer>>()

            while (queue.isNotEmpty()) {
                val item = queue.removeFirst()
                visited += item.computer.name
                if (item.loop.size > 3) continue
                item.computer.connected.forEach {
                    if (!visited.contains(it.name)) {
                        queue += QueueItem(it, item.loop + it)
                    } else {
                        if (it === this) {
                            if (item.loop.size == 3) {
                                loops += item.loop
                            }
                        }
                    }
                }
            }
            return loops
        }
    }
}