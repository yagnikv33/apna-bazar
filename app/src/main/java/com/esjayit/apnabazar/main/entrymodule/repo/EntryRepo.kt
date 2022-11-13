package com.esjayit.apnabazar.main.entrymodule.repo
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.CheckUpdateResponse
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo
import java.util.UUID

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

    suspend fun addDeviceInfo(uuid: String, isRooted: String, installed: String,
        onError: (ApiResult<Any>) -> Unit
    ): AddDeviceInfoResponse? {
        return with(apiCall(executable = { apiCall.addDeviceInfo(
            udid = uuid ,
            isRooted = isRooted,
            installId = installed
        ) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun checkUpdate(onError: (ApiResult<Any>) -> Unit
    ): CheckUpdateResponse? {
        return with(apiCall(executable = { apiCall.checkUpdate() })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}