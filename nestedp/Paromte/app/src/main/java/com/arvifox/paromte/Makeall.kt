package com.arvifox.paromte

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

fun dod(s: String) {
    Log.d("foxxx", s)
}

val okhttp = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
    .build()

val retr = Retrofit.Builder()
    .client(okhttp)
    .baseUrl("https://sorascan.com")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface FoApi {
    @GET("/api/v1/balances/transfer")
    suspend fun getTransactionsPaged(
        @Query("filter[address]") address: String,
        @Query("page[number]") pageNumber: Long,
        @Query("page[size]") pageSize: Int
    ): FoResponse
}

val api = retr.create(FoApi::class.java)

data class FoResponse(val errors: List<Any>, val data: List<FoRemote>)

data class FoRemote(val attributes: FoRemAtt, val id: String)

data class FoRemAtt(
    val value: BigDecimal,
    val fee: BigDecimal,
    val assetId: String,
    val sender: FoAtteer,
    val destination: FoAtteer,
)

data class FoAtteer(val attributes: FoRemoteAttata)

data class FoRemoteAttata(val address: String)