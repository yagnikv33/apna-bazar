package com.esjayit.apnabazar.main.dashboard.repo

import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo

class DashboardRepo(private val apiCall: DashboardApiModule) : BaseRepo() {

    suspend fun getMediumList(
        userid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): MediumResponse? {
        return with(apiCall(executable = {
            apiCall.getMediumList(
                userid = userid,
                installid = installid
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}