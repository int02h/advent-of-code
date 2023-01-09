package aoc2015

import AocDay
import Input
import java.util.Collections

object Day13 : AocDay {

    override fun part1(input: Input) {
        val persons = readPersons(input)
        val permutations = mutableListOf<List<Person>>()
        permute(persons.values.toList(), permutations)
        println(permutations.maxOf { calcScore(it) })
    }

    override fun part2(input: Input) {
        val persons = readPersons(input)
        val me = Person("Me")
        persons.values.forEach { p ->
            p.addPotentialNeighbor(me.name, 0)
            me.addPotentialNeighbor(p.name, 0)
        }
        persons[me.name] = me

        val permutations = mutableListOf<List<Person>>()
        permute(persons.values.toList(), permutations)
        println(permutations.maxOf { calcScore(it) })
    }

    private fun permute(
        persons: List<Person>,
        result: MutableList<List<Person>>,
        l: Int = 0,
        r: Int = persons.lastIndex
    ) {
        if (l == r) {
            result.add(persons)
        } else {
            val list = persons.toMutableList()
            for (i in l..r) {
                Collections.swap(list, l, i)
                permute(list.toMutableList(), result, l + 1, r)
                Collections.swap(list, l, i)
            }
        }
    }

    private fun calcScore(persons: List<Person>): Int {
        var score = 0
        for (i in persons.indices) {
            val person = persons[i]
            score += person.potentialNeighbors.getValue(persons.wrappingGet(i - 1).name)
            score += person.potentialNeighbors.getValue(persons.wrappingGet(i + 1).name)
        }
        return score
    }

    private fun List<Person>.wrappingGet(index: Int): Person {
        if (index < 0) {
            return get(size + index)
        }
        if (index > lastIndex) {
            return get(index - size)
        }
        return get(index)
    }

    private fun readPersons(input: Input): MutableMap<String, Person> {
        val map = mutableMapOf<String, Person>()
        input.asLines()
            .map { it.split(' ') }
            .map {
                val name = it[0]
                val neighborName = it.last().dropLast(1)
                val happinessChange = when (it[2]) {
                    "gain" -> it[3].toInt()
                    "lose" -> -it[3].toInt()
                    else -> error("Bad happiness change: ${it[2]}")
                }
                val person = map.getOrPut(name) { Person(name) }
                person.addPotentialNeighbor(neighborName, happinessChange)
            }
        return map
    }

    private class Person(val name: String) {

        val potentialNeighbors = mutableMapOf<String, Int>()

        fun addPotentialNeighbor(neighborName: String, happinessChange: Int) {
            potentialNeighbors[neighborName] = happinessChange
        }

        override fun toString(): String = name
    }

}