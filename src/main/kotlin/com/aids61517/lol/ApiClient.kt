package com.aids61517.lol

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit

class ApiClient(
    private val port: Int,
    private val password: String,
) {
    private val okHttpClient by lazy {
        val manager = SSLSocketUtil.getX509TrustManager()
        OkHttpClient.Builder()
            .authenticator { _, response ->
                val credential = Credentials.basic("riot", password)
                response.request
                    .newBuilder()
                    .header("Authorization", credential)
                    .build()
            }
            .sslSocketFactory(SSLSocketUtil.getSocketFactory(manager), manager)
            .hostnameVerifier(SSLSocketUtil.getHostnameVerifier())
            .build()
    }

    private val baseJsonParser = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://127.0.0.1:$port/")
        .client(okHttpClient)
        .addConverterFactory(baseJsonParser.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun getPlayerLoot(): Map<String, JsonObject> {
        return execute(apiService.getPlayerLootMap())
    }

    fun craftChampion(count: Int, lootId: String) {
        execute(apiService.craftChampion(count, JsonArray(listOf(JsonPrimitive(lootId)))))
    }

    fun craftRentalChampion(count: Int, lootId: String) {
        execute(apiService.craftRentalChampion(count, JsonArray(listOf(JsonPrimitive(lootId)))))
    }

    private fun <T> execute(call: Call<T>): T {
        val response = call.execute()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IllegalStateException("Response is failed")
        }
    }
}