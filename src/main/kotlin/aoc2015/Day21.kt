package aoc2015

import AocDay
import Input
import kotlin.math.max
import kotlin.math.min

object Day21 : AocDay {

    override fun part1(input: Input) {
        val originalBoss = readBoss(input)
        var minCost = Int.MAX_VALUE

        ShopItem.WEAPONS.forEach { weapon ->
            ShopItem.ARMORS.forEach { armor ->
                ShopItem.RINGS.forEach { ring1 ->
                    ShopItem.RINGS.forEach { ring2 ->
                        if (ring1 !== ring2) {
                            val player = Player(
                                hp = 100,
                                damage = weapon.damage + armor.damage + ring1.damage + ring2.damage,
                                armor = weapon.armor + armor.armor + ring1.armor + ring2.armor
                            )
                            val boss = originalBoss.copy()
                            val cost = weapon.cost + armor.cost + ring1.cost + ring2.cost
                            if (playGame(player, boss)) {
                                minCost = min(minCost, cost)
                            }
                        }
                    }
                }
            }
        }

        println(minCost)
    }

    override fun part2(input: Input) {
        val originalBoss = readBoss(input)
        var maxCost = Int.MIN_VALUE

        ShopItem.WEAPONS.forEach { weapon ->
            ShopItem.ARMORS.forEach { armor ->
                ShopItem.RINGS.forEach { ring1 ->
                    ShopItem.RINGS.forEach { ring2 ->
                        if (ring1 !== ring2) {
                            val player = Player(
                                hp = 100,
                                damage = weapon.damage + armor.damage + ring1.damage + ring2.damage,
                                armor = weapon.armor + armor.armor + ring1.armor + ring2.armor
                            )
                            val boss = originalBoss.copy()
                            val cost = weapon.cost + armor.cost + ring1.cost + ring2.cost
                            if (!playGame(player, boss)) {
                                maxCost = max(maxCost, cost)
                            }
                        }
                    }
                }
            }
        }

        println(maxCost)
    }


    private fun playGame(player: Player, boss: Player): Boolean {
        while (true) {
            player.hit(boss)
            if (boss.hp <= 0) {
                return true
            }
            boss.hit(player)
            if (player.hp <= 0) {
                return false
            }
        }
    }

    private fun readBoss(input: Input): Player {
        val (hp, damage, armor) = input.asLines()
            .map { it.split(' ').last().toInt() }
        return Player(hp, damage, armor)
    }

    private data class ShopItem(
        val name: String,
        val cost: Int,
        val damage: Int,
        val armor: Int
    ) {
        companion object {
            val WEAPONS = listOf(
                ShopItem("Dagger", 8, 4, 0),
                ShopItem("Shortsword", 10, 5, 0),
                ShopItem("Warhammer", 25, 6, 0),
                ShopItem("Longsword", 40, 7, 0),
                ShopItem("Greataxe", 74, 8, 0),
            )
            val ARMORS = listOf(
                ShopItem("Leather", 13, 0, 1),
                ShopItem("Chainmail", 31, 0, 2),
                ShopItem("Splintmail", 53, 0, 3),
                ShopItem("Bandedmail", 75, 0, 4),
                ShopItem("Platemail", 102, 0, 5),
                ShopItem("NO_ARMOR", 0, 0, 0),
            )
            val RINGS = listOf(
                ShopItem("Damage +1", 25, 1, 0),
                ShopItem("Damage +2", 50, 2, 0),
                ShopItem("Damage +3", 100, 3, 0),
                ShopItem("Defense +1", 20, 0, 1),
                ShopItem("Defense +2", 40, 0, 2),
                ShopItem("Defense +3", 80, 0, 3),
                ShopItem("NO_RING_1", 0, 0, 0),
                ShopItem("NO_RING_2", 0, 0, 0),
            )
        }
    }

    private data class Player(
        var hp: Int,
        val damage: Int,
        val armor: Int
    ) {
        fun hit(other: Player) {
            other.hp -= max(1, this.damage - other.armor)
        }
    }

}