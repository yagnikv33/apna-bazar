package com.esjayit.apnabazar.api.service

import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.CHECK_USER_ACTIVE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_HOME_DATA
import com.esjayit.apnabazar.data.model.response.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HomeApiModule {

    @FormUrlEncoded
    @POST(CHECK_USER_ACTIVE)
    suspend fun checkUserActive(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : CheckUserActiveResponse

    @FormUrlEncoded
    @POST(GET_HOME_DATA)
    suspend fun getHomeMessageList(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : CheckUserActiveResponse

}