package com.arvifox.arvi.rep

import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.simplemisc.misc2.models.DaResponse
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitMock {

    fun get(a: String): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(Interc(a)).build()
    }

    fun getR(a: String): IRetr {
        return Retrofit.Builder()
                .baseUrl("http://www.asdf.ru")
                .client(get(a))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(IRetr::class.java)
    }
}

interface IRetr {

    @GET("my_query")
    fun getRe(): Call<DaResponse>
}

class Interc(val answer: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri()
            val qu = uri.query
            val squ = qu?.split("=")
            val re = Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(answer.toResponseBody("application/json".toMediaTypeOrNull()))
                    .addHeader("content-type", "application/json")
                    .build()
            return re
        } else {
            return chain.proceed(chain.request())
        }
    }
}