package com.esjayit.apnabazar.main.entrymodule.repo
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo

class EntryRepo(private val apiCall: EntryApiModule) : BaseRepo() {

    suspend fun getSplashData(
               onError: (ApiResult<Any>) -> Unit
    ): SplashResponse? {
        return with(apiCall(executable = { apiCall.getSplashData() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}