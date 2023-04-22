package com.aids61517.lol

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("lol-loot/v1/player-loot-map")
    fun getPlayerLootMap(): Call<Map<String, JsonObject>>

    @POST("lol-loot/v1/recipes/CHAMPION_disenchant/craft")
    fun craftChampion(@Query("repeat") count: Int, @Body body: JsonArray): Call<Unit>

    @POST("lol-loot/v1/recipes/CHAMPION_RENTAL_disenchant/craft")
    fun craftRentalChampion(@Query("repeat") count: Int, @Body body: JsonArray): Call<Unit>
}