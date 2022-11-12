package com.esjayit.apnabazar.api.service

import com.esjayit.apnabazar.AppConstants.Api.EndUrl.HOME
import com.esjayit.apnabazar.data.model.response.SplashResponse
import retrofit2.http.POST

interface EntryApiModule {

    @POST(HOME)
    suspend fun getSplashData(): SplashResponse
}