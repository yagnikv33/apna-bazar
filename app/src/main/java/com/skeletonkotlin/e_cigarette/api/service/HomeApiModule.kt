package com.skeletonkotlin.e_cigarette.api.service

import com.skeletonkotlin.e_cigarette.AppConstants.Api.EndUrl.PORTAL
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import retrofit2.http.POST

interface HomeApiModule {

    @POST(PORTAL)
    suspend fun getPortalData(): HomeResponse
}