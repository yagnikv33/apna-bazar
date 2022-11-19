package com.esjayit.apnabazar.main.dashboard.repo

import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.data.model.response.GetDemandDataResponse
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.data.model.response.StandardResponse
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

    suspend fun getStandard(
        userid: String,
        userMedium: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): StandardResponse? {
        return with(apiCall(executable = {
            apiCall.getStandardList(
                userid = userid,
                installid = installid,
                userMedium = userMedium
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getSubjectList(
        userid: String,
        userMedium: String,
        standard: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): GetDemandDataResponse? {
        return with(apiCall(executable = {
            apiCall.getSubjectList(
                userid = userid,
                installid = installid,
                standard = standard,
                userMedium = userMedium
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}