import org.reflections.Reflections

fun main() {
    val year = 2024
    val reflections = Reflections("aoc${year}")
    val cls = reflections.getSubTypesOf(AocDay2::class.java)
        .maxBy { it.simpleName!!.dropWhile { ch -> !ch.isDigit() }.toInt() }

    var instance = cls.getDeclaredConstructor().newInstance()
    instance.readInput(Input(year, getDayIndex(instance)))
    var start = System.currentTimeMillis()
    instance.part1()
    println("Part 1 took ${System.currentTimeMillis() - start} ms")

    instance = cls.getDeclaredConstructor().newInstance()
    instance.readInput(Input(year, getDayIndex(instance)))
    start = System.currentTimeMillis()
    instance.part2()
    println("Part 2 took ${System.currentTimeMillis() - start} ms")
}

private fun getDayIndex(instance: AocDay2): Int = instance::class.simpleName!!.takeLastWhile { it.isDigit() }.toInt()
