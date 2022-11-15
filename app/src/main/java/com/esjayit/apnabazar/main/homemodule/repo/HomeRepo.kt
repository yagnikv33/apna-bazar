package com.esjayit.apnabazar.main.homemodule.repo

import com.esjayit.apnabazar.api.service.HomeApiModule
import com.esjayit.apnabazar.data.model.response.CheckUserActiveResponse
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo

class HomeRepo(private val apiCall: HomeApiModule) : BaseRepo() {

    //Check User Active (Home Screen)
    suspend fun checkUserActive(userId: String, installed: String,
                      onError: (ApiResult<Any>) -> Unit
    ): CheckUserActiveResponse? {
        return with(apiCall(executable = {
            apiCall.checkUserActive(
                userId = userId,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}