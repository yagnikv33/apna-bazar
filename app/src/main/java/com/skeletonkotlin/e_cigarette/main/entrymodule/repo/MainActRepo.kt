package com.skeletonkotlin.e_cigarette.main.entrymodule.repo

import com.skeletonkotlin.e_cigarette.api.service.EntryApiModule
import com.skeletonkotlin.e_cigarette.data.model.response.LoginResModel
import com.skeletonkotlin.e_cigarette.main.base.ApiResult
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo

class MainActRepo(private val apiCall: EntryApiModule) : BaseRepo() {

    suspend fun login(
        email: String,
        password: String,
        onError: ((ApiResult<Any>) -> Unit)?
    ): LoginResModel? {
        return with(apiCall(executable = { apiCall.login(email, password) })) {
            if (data == null)
                onError?.invoke(ApiResult(null, resultType, error, resCode = resCode))
            data
        }
    }
}