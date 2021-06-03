package com.yudiz.e_cigarette.main.homemodule.repo

import com.yudiz.e_cigarette.api.service.HomeApiModule
import com.yudiz.e_cigarette.data.model.response.*
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

    suspend fun getFactsList(
        onError: (ApiResult<Any>) -> Unit
    ): KnowFactsResponse? {
        return with(apiCall(executable = { apiCall.getFactsList() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getPgVgData(
        onError: (ApiResult<Any>) -> Unit
    ): PgVgResponse? {
        return with(apiCall(executable = { apiCall.getPgVgData() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getTestimonialsData(
        onError: (ApiResult<Any>) -> Unit
    ): TestimonialsResponse? {
        return with(apiCall(executable = { apiCall.getTestimonialsData() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getPersonaliseVapeData(
        onError: (ApiResult<Any>) -> Unit
    ): PersonaliseVapeResponse? {
        return with(apiCall(executable = { apiCall.getPersonaliseVapeData() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}