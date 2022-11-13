package com.esjayit.apnabazar.main.entrymodule.repo
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo
import com.google.gson.JsonObject

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

    suspend fun checkUpdate(installId: String,
                            onError: (ApiResult<Any>) -> Unit
    ): CheckUpdateResponse? {
        return with(apiCall(executable = { apiCall.checkUpdate(installId = installId) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun appFirstLaunch(fcmToken: String, playerId: String, installId: String, deviceInfoJson: JsonObject,
                               onError: (ApiResult<Any>) -> Unit
    ): AppFirstLaunchResponse? {
        return with(apiCall(executable = { apiCall.appFirstLunch(
            fcmToken = fcmToken,
            playerId = playerId,
            installId = installId,
            deviceInfo = deviceInfoJson
        ) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }


    suspend fun checkUserVerification(userName: String, installed: String,
                                      onError: (ApiResult<Any>) -> Unit
    ): CheckUserVerificationResponse? {
        return with(apiCall(executable = {
            apiCall.checkUserVerification(
                userName = userName,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}