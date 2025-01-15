package aoc2019

import AocDay2
import Input
import java.math.BigInteger

class Day22 : AocDay2 {

    private lateinit var cardsDeals: List<CardDeal>

    override fun readInput(input: Input) {
        cardsDeals = input.asLines().map { line ->
            when {
                line.startsWith("deal into new stack") -> CardDeal.NewStack
                line.startsWith("cut ") -> CardDeal.Cut(line.drop(4).toInt())
                line.startsWith("deal with increment ") -> CardDeal.Increment(line.drop(20).toInt())
                else -> error(line)
            }
        }
    }

    override fun part1() {
        var cards = (0 until 10007).toList()
        cardsDeals.forEach { cards = it.deal(cards) }
        println(cards.indexOf(2019))
    }

    override fun part2() {
        // deck size
        val d = BigInteger.valueOf(119315717514047L)

        fun f(position: BigInteger): BigInteger {
            var p = position
            cardsDeals.reversed().forEach { p = it.reverseDeal(p, d) }
            return p
        }

        val x = BigInteger.valueOf(2020L)
        val y = f(x)
        val z = f(y)
        val a = (y - z) * (x - y + d).modInverse(d) % d
        val b = (y - a * x) % d

        val n = BigInteger.valueOf(101741582076661)
        println(
            (a.modPow(n, d) * x + (a.modPow(n, d) - BigInteger.ONE) * (a - BigInteger.ONE).modInverse(d) * b) % d
        )
    }

    sealed interface CardDeal {

        fun deal(cards: List<Int>): List<Int>
        fun reverseDeal(position: BigInteger, deckSize: BigInteger): BigInteger

        object NewStack : CardDeal {
            override fun deal(cards: List<Int>): List<Int> = cards.reversed()

            override fun reverseDeal(position: BigInteger, deckSize: BigInteger): BigInteger =
                deckSize - position - BigInteger.ONE
        }

        data class Cut(val n: Int) : CardDeal {
            override fun deal(cards: List<Int>): List<Int> =
                if (n < 0) {
                    cards.takeLast(-n) + cards.take(cards.size + n)
                } else {
                    cards.takeLast(cards.size - n) + cards.take(n)
                }

            override fun reverseDeal(position: BigInteger, deckSize: BigInteger): BigInteger =
                (position + BigInteger.valueOf(n.toLong()) + deckSize) % deckSize
        }

        data class Increment(val n: Int) : CardDeal {
            override fun deal(cards: List<Int>): List<Int> {
                val table = ArrayList<Int>(cards)
                var index = 0
                val deck = cards.toMutableList()
                while (deck.isNotEmpty()) {
                    val card = deck.removeFirst()
                    table[index] = card
                    index = (index + n) % table.size
                }
                return table
            }

            override fun reverseDeal(position: BigInteger, deckSize: BigInteger): BigInteger =
                BigInteger.valueOf(n.toLong()).modInverse(deckSize) * position % deckSize
        }
    }
}