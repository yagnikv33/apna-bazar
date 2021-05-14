package com.skeletonkotlin.e_cigarette.main.entrymodule.repo
import com.skeletonkotlin.e_cigarette.api.service.EntryApiModule
import com.skeletonkotlin.e_cigarette.data.model.response.SplashResponse
import com.skeletonkotlin.e_cigarette.main.base.ApiResult
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo

class EntryRepo(private val apiCall: EntryApiModule) : BaseRepo() {

    suspend fun getSplashData(
        company_id: String,
        onError: (ApiResult<Any>) -> Unit
    ): SplashResponse? {
        return with(apiCall(executable = { apiCall.getSplashData(company_id) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}