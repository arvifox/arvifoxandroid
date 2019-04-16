package com.arvifox.arvi.simplemisc.tmdb

import com.arvifox.arvi.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object Apifactory {

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_KEY)
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)
    }

    //OkhttpClient for building http request url
    private val tmdbClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()


    fun retrofit(): Retrofit = Retrofit.Builder()
            .client(tmdbClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    val tmdbApi: TmdbApi = retrofit().create(TmdbApi::class.java)

    interface TmdbApi {
        @GET("movie/popular")
        fun getPopularMovieAsync(): Deferred<Response<TmdbMovieResponse>>

        @GET("movie/{id}")
        fun getMovieById(@Path("id") id: Int): Deferred<Response<TmdbMovie>>
    }

    sealed class Result<out T: Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }
}