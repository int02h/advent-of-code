package aoc2015

import AocDay
import Input
import java.util.regex.Pattern

object Day12 : AocDay {

    override fun part1(input: Input) {
        val json = input.asText()
        val matcher = Pattern.compile("(-?\\d+)").matcher(json)
        var sum = 0
        while (matcher.find()) {
            sum += matcher.group(1).toInt()
        }
        println(sum)
    }

    override fun part2(input: Input) {
        val json = JsonParser(input.asText()).parse()
        var sum = 0
        val red = JsonEntity.JsonString("red")

        fun visit(entity: JsonEntity) {
            when (entity) {
                is JsonEntity.JsonArray -> entity.items.forEach(::visit)
                is JsonEntity.JsonNumber -> sum += entity.value
                is JsonEntity.JsonObject -> {
                    if (!entity.properties.containsValue(red)) {
                        entity.properties.values.forEach(::visit)
                    }
                }
                is JsonEntity.JsonString -> Unit
            }
        }

        visit(json)
        println(sum)
    }

    private class JsonParser(val json: String) {
        var index = 0

        fun parse(): JsonEntity = tryObject() ?: tryArray() ?: error("Bad JSON")

        private fun tryString(): JsonEntity.JsonString? {
            if (json[index] != '"') return null
            return JsonEntity.JsonString(
                buildString {
                    while (json[++index] != '"') {
                        append(json[index])
                    }
                    index++
                }
            )
        }

        private fun tryNumber(): JsonEntity.JsonNumber? {
            if (!json[index].isDigit() && json[index] != '-') return null
            return JsonEntity.JsonNumber(
                buildString {
                    while (json[index].isDigit() || json[index] == '-') {
                        append(json[index++])
                    }
                }.toInt()
            )
        }

        private fun requireKeyValuePair(): Pair<String, JsonEntity> {
            val key = tryString() ?: error("Bad key")
            if (json[index] != ':') error("Bad pair")
            index++
            val value = tryString() ?: tryNumber() ?: tryObject() ?: tryArray() ?: error("Bad value")
            return key.value to value
        }

        private fun tryObject(): JsonEntity.JsonObject? {
            if (json[index] != '{') return null
            val properties = mutableMapOf<String, JsonEntity>()
            index++
            while (json[index] != '}') {
                val (key, value) = requireKeyValuePair()
                properties[key] = value
                if (json[index] == ',') index++
            }
            index++
            return JsonEntity.JsonObject(properties)
        }

        private fun tryArray(): JsonEntity.JsonArray? {
            if (json[index] != '[') return null
            val items = mutableListOf<JsonEntity>()
            index++
            while (json[index] != ']') {
                val value = tryString() ?: tryNumber() ?: tryObject() ?: tryArray() ?: error("Bad value")
                items += value
                if (json[index] == ',') index++
            }
            index++
            return JsonEntity.JsonArray(items)
        }

    }

    private sealed class JsonEntity {
        data class JsonObject(val properties: Map<String, JsonEntity>) : JsonEntity()
        data class JsonArray(val items: List<JsonEntity>) : JsonEntity()
        data class JsonString(val value: String) : JsonEntity()
        data class JsonNumber(val value: Int) : JsonEntity()
    }

}