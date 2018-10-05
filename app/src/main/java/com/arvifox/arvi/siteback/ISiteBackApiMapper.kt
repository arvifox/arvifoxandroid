package com.arvifox.arvi.siteback

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ISiteBackApiMapper {

    @GET("adddevice.php?id={token}")
    fun addDevice(@Path("token") token: String): Single<Boolean>
}