package aoc2022

import Input

object Day13 {

    fun part1(input: Input) {
        var sum = 0
        readPairs(input).forEachIndexed { index, (p1, p2) ->
            if (p1 <= p2) {
                sum += index + 1
            }
        }
        println(sum)
    }

    fun part2(input: Input) {
        val pairs = readPairs(input).flatMap { it.toList() }.toMutableList()
        val divider1 = Packet.parse("[[2]]")
        pairs.add(divider1)
        val divider2 = Packet.parse("[[6]]")
        pairs.add(divider2)
        pairs.sort()

        println(
            (pairs.indexOf(divider1) + 1) * (pairs.indexOf(divider2) + 1)
        )
    }

    private fun readPairs(input: Input): List<Pair<Packet, Packet>> =
        input.asText()
            .split("\n\n")
            .map { it.split('\n') }
            .map { (p1, p2) -> Packet.parse(p1) to Packet.parse(p2) }

    class Packet(private val items: PacketItem.ItemList) : Comparable<Packet> {
        companion object {
            fun parse(value: String): Packet = Packet(readList(CharStream(value)))

            private fun readList(stream: CharStream): PacketItem.ItemList {
                val items = mutableListOf<PacketItem>()
                stream.moveToNext()
                while (stream.current != ']') {
                    when {
                        stream.current.isDigit() -> items += readValue(stream)
                        stream.current == '[' -> items += readList(stream)
                        else -> stream.moveToNext()
                    }
                }
                stream.moveToNext()
                return PacketItem.ItemList(items)
            }

            private fun readValue(stream: CharStream): PacketItem.ItemValue {
                var digits = ""
                while (stream.current.isDigit()) {
                    digits += stream.current
                    stream.moveToNext()
                }
                return PacketItem.ItemValue(digits.toInt())
            }
        }

        override fun compareTo(other: Packet): Int = this.items.compareTo(other.items)

        override fun toString(): String = items.toString()


    }

    sealed class PacketItem : Comparable<PacketItem> {
        class ItemValue(private val value: Int) : PacketItem() {
            override fun compareTo(other: PacketItem): Int =
                when (other) {
                    is ItemList -> ItemList(listOf(this)).compareTo(other)
                    is ItemValue -> this.value.compareTo(other.value)
                }

            override fun toString(): String = value.toString()
        }

        class ItemList(private val items: List<PacketItem>) : PacketItem() {
            override fun compareTo(other: PacketItem): Int =
                when (other) {
                    is ItemList -> compareList(other)
                    is ItemValue -> compareTo(ItemList(listOf(other)))
                }

            override fun toString(): String = items.joinToString(separator = ",", prefix = "[", postfix = "]")

            private fun compareList(other: ItemList): Int {
                val lastIndex = this.items.lastIndex.coerceAtMost(other.items.lastIndex)
                for (i in 0..lastIndex) {
                    val thisItem = this.items[i]
                    val otherItem = other.items[i]
                    val result = thisItem.compareTo(otherItem)
                    if (result != 0) {
                        return result
                    }
                }
                return this.items.size.compareTo(other.items.size)
            }
        }
    }

    class CharStream(private val value: String) {
        private var index = 0

        val current: Char
            get() = value[index]

        fun moveToNext() {
            index++
        }
    }
}