package aoc2019

import AocDay2
import Input

class Day23 : AocDay2 {

    private lateinit var program: LongArray

    override fun readInput(input: Input) {
        program = input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
    }

    override fun part1() {
        val network = mutableMapOf(255 to NetworkInput(255))
        val computers = (0 until 50).map { address ->
            val input = NetworkInput(address)
            network[address] = input
            val output = NetworkOutput(network)
            IntCodeComputer(program, input, output)
        }
        while (network.getValue(255).size < 2) {
            computers.forEach { it.executeNext() }
        }
        println(network.getValue(255).last())
    }

    override fun part2() {
        val network = mutableMapOf<Int, MutableList<Long>>()
        val nat = NAT(network)
        network[255] = nat

        val computers = (0 until 50).map { address ->
            val input = NetworkInput(address, nat)
            network[address] = input
            val output = NetworkOutput(network)
            IntCodeComputer(program, input, output)
        }
        while (nat.result == -1L) {
            computers.forEach { it.executeNext() }
        }
        println(nat.result)
    }

    private class NAT(private val network: Map<Int, MutableList<Long>>) : ArrayList<Long>() {

        private val values = LongArray(2)
        private var index = 0
        private val idle = mutableSetOf<Int>()
        private var lastY: Long = -1

        var result = -1L

        override fun add(element: Long): Boolean {
            values[index] = element
            index = (index + 1) % values.size
            return true
        }

        fun setIdle(address: Int, isIdle: Boolean) {
            if (isIdle) {
                idle += address
                if (idle.size == 50) {
                    network.getValue(0).add(values[0])
                    network.getValue(0).add(values[1])
                    if (values[1] == lastY) {
                        result = values[1]
                    }
                    lastY = values[1]
                    idle.clear()
                }
            } else {
                idle -= address
            }
        }
    }

    private class NetworkInput(
        private val address: Int,
        private val nat: NAT? = null
    ) : ArrayList<Long>() {
        init {
            add(address.toLong())
        }

        override fun isEmpty(): Boolean = false

        override fun removeAt(index: Int): Long {
            if (super.isEmpty()) {
                nat?.setIdle(address, true)
                return -1
            }
            nat?.setIdle(address, false)
            return super.removeAt(index)
        }
    }

    private class NetworkOutput(
        private val network: Map<Int, MutableList<Long>>,
        private val nat: NAT? = null
    ) : ArrayList<Long>() {

        private var index = 0
        private val values = LongArray(3)

        override fun add(element: Long): Boolean {
            values[index++] = element
            if (index == values.size) {
                index = 0
                val address = values[0].toInt()
                network.getValue(address).add(values[1])
                network.getValue(address).add(values[2])
                nat?.setIdle(address, false)
            }
            return true
        }
    }
}