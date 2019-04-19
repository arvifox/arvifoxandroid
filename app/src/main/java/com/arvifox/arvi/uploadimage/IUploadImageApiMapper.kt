package com.arvifox.arvi.uploadimage

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface IUploadImageApiMapper {
    @Multipart
    @POST("/kjfds")
    fun editUser(@Part fname: MultipartBody.Part, @Part("id") id: RequestBody): Call<ResponseBody>
}