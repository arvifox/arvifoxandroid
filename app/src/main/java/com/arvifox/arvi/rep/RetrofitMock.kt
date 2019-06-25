package com.arvifox.arvi.rep

import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.simplemisc.misc2.Tomapin
import okhttp3.*
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
    fun getRe(): Call<List<Tomapin>>
}

class Interc(val answer: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url().uri()
            val qu = uri.query
            val squ = qu?.split("=")
            val re = Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), answer))
                    .addHeader("content-type", "application/json")
                    .build()
            return re
        } else {
            return chain.proceed(chain.request())
        }
    }
}