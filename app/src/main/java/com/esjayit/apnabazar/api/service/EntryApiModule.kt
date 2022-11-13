package com.esjayit.apnabazar.api.service

import android.os.Build
import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_DEVICE_INFO
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.CHECK_UPDATE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.HOME
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.CheckUpdateResponse
import com.esjayit.apnabazar.data.model.response.SplashResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.UUID

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
        @Field("installid") installId : String = ""
    ) : CheckUpdateResponse


}