package com.yudiz.e_cigarette.main.homemodule.repo

import com.yudiz.e_cigarette.api.service.HomeApiModule
import com.yudiz.e_cigarette.data.model.response.BrandItemResponse
import com.yudiz.e_cigarette.data.model.response.HomeResponse
import com.yudiz.e_cigarette.data.model.response.OurBrandsResponse
import com.yudiz.e_cigarette.data.model.response.VapingResponse
import com.yudiz.e_cigarette.main.base.ApiResult
import com.yudiz.e_cigarette.main.base.BaseRepo

class HomeRepo(private val apiCall: HomeApiModule) : BaseRepo() {

    suspend fun getPortalData(
        onError: (ApiResult<Any>) -> Unit
    ): HomeResponse? {
        return with(apiCall(executable = { apiCall.getPortalData() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getBrandData(
        brand_id: String,
        onError: (ApiResult<Any>) -> Unit
    ): BrandItemResponse? {
        return with(apiCall(executable = { apiCall.getBrandData(brand_id) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getBrandList(
        onError: (ApiResult<Any>) -> Unit
    ): OurBrandsResponse? {
        return with(apiCall(executable = { apiCall.getBrandList() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getVapingList(
        onError: (ApiResult<Any>) -> Unit
    ): VapingResponse? {
        return with(apiCall(executable = { apiCall.getVapingList() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}