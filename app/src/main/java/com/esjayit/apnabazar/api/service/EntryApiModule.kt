package com.esjayit.apnabazar.api.service

import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_DEVICE_INFO
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.HOME
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.SplashResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EntryApiModule {

    @POST(HOME)
    suspend fun getSplashData(): SplashResponse

    @FormUrlEncoded
    @POST(ADD_DEVICE_INFO)
    suspend fun addDeviceInfo(
        @Field("VERSION_RELEASE") version_relese : String
    ) : AddDeviceInfoResponse



}