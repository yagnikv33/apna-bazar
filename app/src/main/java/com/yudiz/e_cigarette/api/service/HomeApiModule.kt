package com.yudiz.e_cigarette.api.service

import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.BRAND_DETAIL
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.BRAND_LIST
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.PORTAL
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.VAPING_LIST
import com.yudiz.e_cigarette.data.model.response.BrandItemResponse
import com.yudiz.e_cigarette.data.model.response.HomeResponse
import com.yudiz.e_cigarette.data.model.response.OurBrandsResponse
import com.yudiz.e_cigarette.data.model.response.VapingResponse
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

    @POST(BRAND_LIST)
    suspend fun getBrandList(): OurBrandsResponse

    @POST(VAPING_LIST)
    suspend fun getVapingList(): VapingResponse
}