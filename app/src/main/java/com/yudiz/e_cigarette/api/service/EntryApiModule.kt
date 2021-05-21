package com.yudiz.e_cigarette.api.service

import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.HOME
import com.yudiz.e_cigarette.data.model.response.SplashResponse
import retrofit2.http.POST

interface EntryApiModule {

    @POST(HOME)
    suspend fun getSplashData(): SplashResponse
}