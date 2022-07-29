package com.arvifox.arvi.siteback

import retrofit2.http.GET
import retrofit2.http.Path

interface ISiteBackApiMapper {

    @GET("adddevice.php?id={token}")
    suspend fun addDevice(@Path("token") token: String): Boolean
}