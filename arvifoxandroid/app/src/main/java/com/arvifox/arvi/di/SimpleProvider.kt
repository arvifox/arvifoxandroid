package com.arvifox.arvi.di

import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.uploadimage.IUploadImageApiMapper
import com.arvifox.arvi.utils.IPermissionManager
import com.arvifox.arvi.utils.PermissionManager
import retrofit2.Retrofit

object SimpleProvider {

    var permissionManager: IPermissionManager = PermissionManager()

    var uploadImageApiMapper: IUploadImageApiMapper = Retrofit.Builder()
            .baseUrl(BuildConfig.ARVI_API_URL)
            .build()
            .create(IUploadImageApiMapper::class.java)
}