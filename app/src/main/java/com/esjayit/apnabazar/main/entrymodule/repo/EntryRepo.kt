package com.esjayit.apnabazar.main.entrymodule.repo
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo
import com.google.gson.JsonObject

class EntryRepo(private val apiCall: EntryApiModule) : BaseRepo() {

    var sendOTPData = mutableListOf<SendOTPResponse?>()

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

    //Send OTP
    suspend fun sendOTP(userName: String, installed: String,
                                      onError: (ApiResult<Any>) -> Unit
    ): SendOTPResponse? {
        return with(apiCall(executable = {
            apiCall.sendOTP(
                userName = userName,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Verify OTP
    suspend fun verifyOTP(otpId: String, otp: String, installed: String,
                        onError: (ApiResult<Any>) -> Unit
    ): VerifyOTPResponse? {
        return with(apiCall(executable = {
            apiCall.verifyOTP(
                otpId = otpId,
                otp = otp,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //New Password
    suspend fun newPassword(userName: String, password: String, installed: String,
                      onError: (ApiResult<Any>) -> Unit
    ): NewPasswordResponse? {
        return with(apiCall(executable = {
            apiCall.newPassword(
                userName = userName,
                password = password,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Login
    suspend fun login(userName: String, password: String, installed: String,
                          onError: (ApiResult<Any>) -> Unit
    ): LoginResponse? {
        return with(apiCall(executable = {
            apiCall.login(
                userName = userName,
                password = password,
                installId = installed,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //All Application Error Log Send to Server
    suspend fun logErrorAdd(
        uuid: String,
        logError: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): CommonResponse? {
        return with(apiCall(executable = {
            apiCall.logError(
                uuid = uuid,
                errorLog = logError,
                installId = installId,
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}