package com.skeletonkotlin.e_cigarette.main.home.repo

import com.skeletonkotlin.e_cigarette.api.service.HomeApiModule
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import com.skeletonkotlin.e_cigarette.main.base.ApiResult
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo

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

}