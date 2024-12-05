package aoc2024

import AocDay
import Input

object Day5 : AocDay {

    override fun part1(input: Input) {
        val updates = readInput(input)
        println(
            updates.filter { it.checkOrder() }.sumOf { it.getMiddlePageNumber() }
        )
    }

    override fun part2(input: Input) {
        val updates = readInput(input)
        println(
            updates.filter { !it.checkOrder() }.sumOf { it.order().getMiddlePageNumber() }
        )
    }

    private fun readInput(input: Input): List<Update> {
        val (rulesRaw, updatesRaw) = input.asText().split("\n\n")
        val pageMap = mutableMapOf<Int, Page>()

        rulesRaw.split("\n").forEach { rule ->
            val (p1, p2) = rule.split("|").map { it.toInt() }
            val page1 = pageMap.getOrPut(p1) { Page(p1) }
            val page2 = pageMap.getOrPut(p2) { Page(p2) }
            page1.before += page2
        }

        val updates = updatesRaw.split("\n")
            .map { update ->
                Update(pageMap, update.split(",").map { it.toInt() })
            }

        return updates
    }

    class Page(
        val number: Int
    ) {
        val before: MutableList<Page> = mutableListOf()

        fun isBefore(page: Page): Boolean {
            return isBeforeInternal(mutableSetOf(), before, page)
        }

        private fun isBeforeInternal(visited: MutableSet<Page>, pages: List<Page>, target: Page, ): Boolean {
            for (p in pages) {
                if (p == target) {
                    return true
                } else {
                    if (!visited.contains(p) && p != this) {
                        visited += p
                        isBeforeInternal(visited, p.before, target)
                    }
                }
            }
            return false
        }

        override fun equals(other: Any?): Boolean {
            return (other as? Page)?.number == number
        }

        override fun hashCode(): Int {
            return number.hashCode()
        }

        override fun toString(): String {
            return number.toString()
        }
    }

    data class Update(
        val pageMap: Map<Int, Page>,
        val pageNumbers: List<Int>
    ) {
        fun checkOrder(): Boolean {
            val visited = mutableSetOf(pageNumbers[0])
            return checkOrderInternal(visited, pageMap.getValue(pageNumbers[0]).before, 1)
        }

        private fun checkOrderInternal(visited: MutableSet<Int>, pages: List<Page>, index: Int): Boolean {
            if (index == pageNumbers.size) {
                return true
            }
            if (visited.contains(pageNumbers[index])) {
                return false
            }
            visited += pageNumbers[index]
            return pages.any { p ->
                if (p.number == pageNumbers[index]) {
                    checkOrderInternal(visited, p.before, index + 1)
                } else {
                    checkOrderInternal(visited, p.before, index)
                }
            }
        }

        fun getMiddlePageNumber(): Int = pageNumbers[pageNumbers.size / 2]

        fun order(): Update {
            val pages = pageNumbers.map { pageMap.getValue(it) }
            val ordered = Update(
                pageMap,
                pageNumbers = pages
                    .sortedWith({ p1, p2 ->
                        if (p1.number == p2.number) {
                            error("Must not happen")
                        }
                        if (p1.isBefore(p2)) -1 else 1
                    })
                    .map { it.number }
            )
            return ordered
        }

    }
}