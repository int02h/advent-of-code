import java.io.File

class Input(year: Int, day: Int) {

    private val file: File = File("input").resolve(year.toString()).resolve("day$day.txt")

    fun asLines(): List<String> = file.readLines()

    fun asText(): String = file.readText()
}