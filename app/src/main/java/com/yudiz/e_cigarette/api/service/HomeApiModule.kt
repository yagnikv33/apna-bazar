package com.yudiz.e_cigarette.api.service

import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.BRAND_DETAIL
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.BRAND_LIST
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.KNOW_THE_FACTS
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.PG_VG
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.PORTAL
import com.yudiz.e_cigarette.AppConstants.Api.EndUrl.VAPING_LIST
import com.yudiz.e_cigarette.AppConstants.App.Buttons.PG_VS_VG
import com.yudiz.e_cigarette.data.model.response.*
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

    @POST(KNOW_THE_FACTS)
    suspend fun getFactsList(): KnowFactsResponse

    @POST(PG_VG)
    suspend fun getPgVgData(): PgVgResponse
}