package aoc2017

import AocDay2
import Input
import util.findAllNumbers

class Day7 : AocDay2 {

    private val towerMap = mutableMapOf<String, Tower>()

    override fun readInput(input: Input) {
        input.asLines().map { line ->
            val parts = line.split(" -> ")
            val fromTowerName = parts[0].substring(0 until parts[0].indexOf(' '))
            val fromTower = towerMap.getOrPut(fromTowerName) { Tower(fromTowerName) }
            fromTower.ownWeight = parts[0].findAllNumbers().first()
            parts.getOrNull(1)?.split(", ")?.forEach { toTowerName ->
                val toTower = towerMap.getOrPut(toTowerName) { Tower(toTowerName) }
                fromTower.towers += toTower
            }
        }
    }

    override fun part1() {
        println(getRoot().name)
    }

    override fun part2() {
        findDisbalance(getRoot())

    }

    private fun findDisbalance(tower: Tower) {
        val weightToTowers = mutableMapOf<Int, MutableList<Tower>>()
        tower.towers.forEach {
            weightToTowers.getOrPut(it.totalWeight) { mutableListOf() } += it
        }
        val correctSize = weightToTowers.filter { it.value.size > 1 }.keys.first()
        val incorrectSize = weightToTowers.filter { it.value.size == 1 }.keys.first()
        val incorrectTower = weightToTowers.getValue(incorrectSize).first()
        if (incorrectTower.isBalanced()) {
            val diff = correctSize - incorrectSize
            println(incorrectTower.ownWeight + diff)
        } else {
            findDisbalance(incorrectTower)
        }
    }

    private fun getRoot(): Tower {
        val towers = towerMap.keys.toMutableSet()
        towerMap.values.forEach { t ->
            t.towers.forEach { towers -= it.name }
        }
        if (towers.size != 1) error("Oops")
        return towerMap.getValue(towers.first())
    }

    private class Tower(val name: String) {
        var ownWeight: Int = 0
        val towers = mutableListOf<Tower>()

        val totalWeight: Int by lazy { ownWeight + towers.sumOf { it.totalWeight } }

        fun isBalanced(): Boolean {
            return towers.map { it.totalWeight }.toSet().size == 1
        }

        override fun toString(): String = name
    }

}