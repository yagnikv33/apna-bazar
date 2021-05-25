package com.yudiz.e_cigarette.api.service

import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.BRAND_DETAIL
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.PORTAL
import com.yudiz.e_cigarette.data.model.response.BrandItemResponse
import com.yudiz.e_cigarette.data.model.response.HomeResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HomeApiModule {

    @POST(PORTAL)
    suspend fun getPortalData(): HomeResponse

    @FormUrlEncoded
    @POST(BRAND_DETAIL)
    suspend fun getBrandData(
        @Field("brand_id") brand_id: String
    ): BrandItemResponse
}