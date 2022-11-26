package com.esjayit.apnabazar.api.service

import android.os.Build
import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_DEVICE_INFO
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.APP_FIRST_LAUNCH_STATUS
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.CHECK_UPDATE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.CHECK_USER_VERIFICATION
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.HOME
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.LOGIN
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.LOG_ERROR
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.NEW_PASSWORD
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.SEND_OTP
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.VERIFY_OTP
import com.esjayit.apnabazar.data.model.response.*
import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EntryApiModule {

    @POST(HOME)
    suspend fun getSplashData(): SplashResponse

    @FormUrlEncoded
    @POST(ADD_DEVICE_INFO)
    suspend fun addDeviceInfo(
        @Field("VERSION_RELEASE") version_relese : String = Build.VERSION.RELEASE,
        @Field("VERSION_INCREMENTAL") version_incremental : String = Build.VERSION.INCREMENTAL,
        @Field("VERSION_SDK_INT") version_sdk_int : String = Build.VERSION.SDK_INT.toString(),
        @Field("BOARD") board : String = Build.BOARD,
        @Field("BOOTLOADER") bootloader : String = Build.BOOTLOADER,
        @Field("BRAND") brand : String = Build.BRAND,
        @Field("CPU_ABI") cpu_abi : String = Build.CPU_ABI,
        @Field("CPU_ABI2") cpu_abi2 : String = Build.CPU_ABI2,
        @Field("DISPLAY") display : String = Build.DISPLAY,
        @Field("FINGERPRINT") fingerprint : String = Build.FINGERPRINT,
        @Field("HARDWARE") hardware : String = Build.HARDWARE,
        @Field("HOST") host : String = Build.HOST,
        @Field("ID") id : String = Build.ID,
        @Field("MANUFACTURER") manufacturer : String = Build.MANUFACTURER,
        @Field("MODEL") model : String = Build.MODEL,
        @Field("PRODUCT") product : String = Build.PRODUCT,
        @Field("SERIAL") serial : String = Build.SERIAL,
        @Field("TAGS") tags : String = Build.TAGS,
        @Field("TIME") time : String = Build.TIME.toString(),
        @Field("TYPE") type : String = Build.TYPE,
        @Field("UNKNOWN") unknown : String = Build.UNKNOWN,
        @Field("USER") user : String = Build.USER,
        @Field("UDID") udid : String,
        @Field("ISROOTED") isRooted : String,
        @Field("APPPACKAGENAME") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("APPVERCODE") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : AddDeviceInfoResponse

    @FormUrlEncoded
    @POST(CHECK_UPDATE)
    suspend fun checkUpdate(
        @Field("APPPACKAGENAME") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("APPVERCODE") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : CheckUpdateResponse


    @FormUrlEncoded
    @POST(APP_FIRST_LAUNCH_STATUS)
    suspend fun appFirstLunch(
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("fcmtoken") fcmToken : String,
        @Field("playerid") playerId : String,
        @Field("installid") installId : String,
        @Field("deviceinfo") deviceInfo: JsonObject
    ) : AppFirstLaunchResponse


    @FormUrlEncoded
    @POST(CHECK_USER_VERIFICATION)
    suspend fun checkUserVerification(
        @Field("username") userName : String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : CheckUserVerificationResponse

    @FormUrlEncoded
    @POST(SEND_OTP)
    suspend fun sendOTP(
        @Field("username") userName : String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : SendOTPResponse

    @FormUrlEncoded
    @POST(VERIFY_OTP)
    suspend fun verifyOTP(
        @Field("otpid") otpId : String,
        @Field("otp") otp : String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : VerifyOTPResponse

    @FormUrlEncoded
    @POST(NEW_PASSWORD)
    suspend fun newPassword(
        @Field("username") userName: String,
        @Field("password") password : String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : NewPasswordResponse


    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") password : String,
        @Field("packagename") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : LoginResponse

    @FormUrlEncoded
    @POST(LOG_ERROR)
    suspend fun logError(
        @Field("UDID") uuid: String,
        @Field("ERRORLOG") errorLog : String,
        @Field("APPPACKAGENAME") appPackgeName : String = BuildConfig.APPLICATION_ID,
        @Field("APPVERCODE") appVerCode : String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId : String
    ) : CommonResponse

}