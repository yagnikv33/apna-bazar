package com.esjayit.apnabazar.api.service

import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_MEDIUM
import com.esjayit.apnabazar.data.model.response.CheckUserActiveResponse
import com.esjayit.apnabazar.data.model.response.MediumResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DashboardApiModule {
    @FormUrlEncoded
    @POST(AppConstants.Api.EndUrl.CHECK_USER_ACTIVE)
    suspend fun checkUserActive(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): CheckUserActiveResponse

    @FormUrlEncoded
    @POST(AppConstants.Api.EndUrl.GET_HOME_DATA)
    suspend fun getHomeMessageList(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): CheckUserActiveResponse

    @FormUrlEncoded
    @POST(GET_MEDIUM)
    suspend fun getMediumList(
        @Field("userid") userid: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): MediumResponse
}