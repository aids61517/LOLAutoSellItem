package com.aids61517.lol

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import okio.FileSystem
import okio.Path.Companion.toPath


fun main(args: Array<String>) = runBlocking {
    val path = "F:\\Riot Games\\League of Legends\\lockfile".toPath()
    if (FileSystem.SYSTEM.exists(path).not()) {
        return@runBlocking
    }

    val content = FileSystem.SYSTEM.read(path) {
        readUtf8()
    }
    println("content = $content")
    val splitArray = content.split(":")
    val port = splitArray[2].toInt()
    val password = splitArray[3]
    println("port = $port")
    println("password = $password")
    val apiClient = ApiClient(port, password)
    val playerLootMap = apiClient.getPlayerLoot()
    val championKey = playerLootMap.filterKeys { it.matches("CHAMPION_\\d+".toRegex()) }
    championKey.forEach { (name, value) ->
        val itemStatus = value.getValue("itemStatus").jsonPrimitive.content
        if (itemStatus == "OWNED") {
            val count = value.getValue("count").jsonPrimitive.int
            apiClient.craftChampion(count, name)
            delay(1000)
        }
    }
    val championRentalKey = playerLootMap.filterKeys { it.matches("CHAMPION_RENTAL_\\d+".toRegex()) }
    championRentalKey.forEach { (name, value) ->
        val itemStatus = value.getValue("itemStatus").jsonPrimitive.content
        if (itemStatus == "OWNED") {
            val count = value.getValue("count").jsonPrimitive.int
            apiClient.craftRentalChampion(count, name)
            delay(1000)
        }
    }
}
