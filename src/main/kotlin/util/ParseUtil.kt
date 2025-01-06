package util

import java.util.regex.Pattern

fun String.findAllNumbers(): List<Int> {
    val result = mutableListOf<Int>()
    val pattern = Pattern.compile("(\\d+)")
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        result += matcher.group().toInt()
    }
    return result
}