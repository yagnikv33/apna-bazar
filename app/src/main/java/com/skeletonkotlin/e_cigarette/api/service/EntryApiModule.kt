package com.skeletonkotlin.e_cigarette.api.service

import com.skeletonkotlin.e_cigarette.AppConstants.Api.EndUrl.HOME
import com.skeletonkotlin.e_cigarette.AppConstants.Api.EndUrl.LOGIN
import com.skeletonkotlin.e_cigarette.data.model.response.LoginResModel
import com.skeletonkotlin.e_cigarette.data.model.response.SplashResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EntryApiModule {

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun login(
        @Field("username") uName: String,
        @Field("password") pass: String
    ): LoginResModel

    @FormUrlEncoded
    @POST(HOME)
    suspend fun getSplashData(
        @Field("company-id") company_id:String
    ):SplashResponse
}



