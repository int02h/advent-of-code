package aoc2023

import AocDay
import Input

object Day7 : AocDay {

    private val typeDetector = TypeDetector { hand ->
        val map = mutableMapOf<Char, Int>()
        hand.forEach { c -> map[c] = map.getOrDefault(c, 0) + 1 }

        when (map.size) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> {
                val (a, b) = map.values.sorted()
                when {
                    a == 1 && b == 4 -> HandType.FOUR_OF_A_KIND
                    a == 2 && b == 3 -> HandType.FULL_HOUSE
                    else -> error("Impossible")
                }
            }
            3 -> {
                val (_, b, c) = map.values.sorted()
                when {
                    c == 3 -> HandType.THREE_OF_A_KIND
                    b == 2 && c == 2 -> HandType.TWO_PAIR
                    else -> error("Impossible")
                }
            }
            4 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    override fun part1(input: Input) {
        assert(typeDetector.detectType("AAAAA") == HandType.FIVE_OF_A_KIND)
        assert(typeDetector.detectType("AA8AA") == HandType.FOUR_OF_A_KIND)
        assert(typeDetector.detectType("23332") == HandType.FULL_HOUSE)
        assert(typeDetector.detectType("TTT98") == HandType.THREE_OF_A_KIND)
        assert(typeDetector.detectType("23432") == HandType.TWO_PAIR)
        assert(typeDetector.detectType("A23A4") == HandType.ONE_PAIR)
        assert(typeDetector.detectType("23456") == HandType.HIGH_CARD)

        val pairs = parse(
            input = input,
            typeDetector = typeDetector,
            cardOrder = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
        )
        val sorted = pairs.sorted()
        println(
            sorted.foldIndexed(0) { index, acc, p -> acc + p.bid * (index + 1) }
        )
    }

    override fun part2(input: Input) {
        val jokerTypeDetector = TypeDetector { hand ->
            val type = typeDetector.detectType(hand)
            if (hand.contains('J')) {
                val map = mutableMapOf<Char, Int>()
                hand.forEach { c -> map[c] = map.getOrDefault(c, 0) + 1 }

                when (type) {
                    HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                    HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                    HandType.FULL_HOUSE -> HandType.FIVE_OF_A_KIND
                    HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND
                    HandType.TWO_PAIR -> if (map['J'] == 2) HandType.FOUR_OF_A_KIND else HandType.FULL_HOUSE
                    HandType.ONE_PAIR -> HandType.THREE_OF_A_KIND
                    HandType.HIGH_CARD -> HandType.ONE_PAIR
                }
            } else {
                type
            }
        }

        assert(jokerTypeDetector.detectType("QJJQ2") == HandType.FOUR_OF_A_KIND)

        val pairs = parse(
            input = input,
            typeDetector = jokerTypeDetector,
            cardOrder = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'))
        val sorted = pairs.sorted()
        println(
            sorted.foldIndexed(0) { index, acc, p -> acc + p.bid * (index + 1) }
        )
    }

    private fun parse(
        input: Input,
        typeDetector: TypeDetector,
        cardOrder: Array<Char>
    ): List<HandAndBid> {
        return input.asLines().map {
            val (hand, bid) = it.split(" ")
            HandAndBid(hand, bid.toInt(), typeDetector, cardOrder)
        }
    }

    private fun interface TypeDetector {
        fun detectType(hand: String): HandType
    }

    private data class HandAndBid(
        val hand: String,
        val bid: Int,
        private val typeDetector: TypeDetector,
        private val cardOrder: Array<Char>
    ) : Comparable<HandAndBid> {

        override fun compareTo(other: HandAndBid): Int {
            val thisType = this.detectType()
            val otherType = other.detectType()
            if (thisType != otherType) {
                return otherType.ordinal - thisType.ordinal
            }
            var index = 0
            while (index < this.hand.length) {
                val thisIndex = cardOrder.indexOf(this.hand[index])
                val otherIndex = cardOrder.indexOf(other.hand[index])
                if (thisIndex != otherIndex) {
                    return otherIndex - thisIndex
                }
                index++
            }
            return 0
        }

        fun detectType(): HandType = typeDetector.detectType(hand)

    }

    private enum class HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

}