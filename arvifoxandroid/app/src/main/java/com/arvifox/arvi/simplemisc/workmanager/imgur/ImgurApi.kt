package com.arvifox.arvi.simplemisc.workmanager.imgur

import android.net.Uri
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

/**
 * The Imgur API client, which uses the [ImgurService] Retrofit APIs.
 */
class ImgurApi private constructor() {
    private val mImgurService: ImgurService

    init {
        val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        mImgurService = retrofit.create(ImgurService::class.java)
    }

    fun uploadImage(imageUri: Uri): Call<PostImageResponse> {
        val imageFile = File(imageUri.path!!)
        val requestFile = RequestBody.create(MEDIA_TYPE_PNG, imageFile)
        val body = MultipartBody.Part.createFormData("image", "image.png", requestFile)
        return mImgurService.postImage(body)
    }

    private class AuthInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val headers = request.headers.newBuilder()
                    .add("Authorization", "Client-ID " + "IMGUR_CLIENT_ID")
                    .build()
            val authenticatedRequest = request.newBuilder().headers(headers).build()
            return chain.proceed(authenticatedRequest)
        }
    }

    companion object {
        private val MEDIA_TYPE_PNG = "image/png".toMediaTypeOrNull()

        val instance: Lazy<ImgurApi> = lazy { ImgurApi() }
    }
}
