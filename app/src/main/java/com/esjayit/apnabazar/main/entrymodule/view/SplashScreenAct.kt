package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.CheckUpdateResponse
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivitySplashScreenBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.JsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SplashScreenAct :
    BaseAct<ActivitySplashScreenBinding, EntryVM>(Layouts.activity_splash_screen) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null
    var uuid: String = UUID.randomUUID().toString()

    override fun init() {
        "UUID ${Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)} RandomUUID : ${uuid}".logE()
        checkForLaunchAPIs()
    }

    //For Launch App API Calling
    fun checkForLaunchAPIs(){
        if(prefs.firstTime) {
            // First Time Launch
            "RUN : First Time".logE()
//            "DATA JSON, ${convertedJSONObject()}".logE()
            vm.checkForUpdate(installedId = uuid)
            vm.addDeviceInfo(uuid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID), isRooted = "0", installedId = uuid)
//            vm.appFirstTimeLaunch(fcmToken = "", installId = uuid, playerId = "", deviceInfoJson = convertedJSONObject())
            prefs.installId = uuid
            prefs.firstTime = false
        } else {
            // App is not First Time Launch
            "RUN : Not First Time".logE()
            if (TextUtils.isEmpty(prefs.installId)) {
                "InstallId is Empty Please check it!!!!".logE()
            } else {
                "Found InstallId ${prefs.installId!!}".logE()
                vm.checkForUpdate(installedId = prefs.installId!!)
            }
        }
    }

    fun convertedJSONObject(): JsonObject {
        return JsonObject().apply {
            this.addProperty("VERSION_INCREMENTAL", Build.VERSION.INCREMENTAL)
            this.addProperty("VERSION_RELEASE", Build.VERSION.RELEASE)
            this.addProperty("VERSION_SDK_INT", Build.VERSION.SDK_INT.toString())
            this.addProperty("BOARD", Build.BOARD)
            this.addProperty("BOOTLOADER", Build.BOOTLOADER)
            this.addProperty("BRAND", Build.BRAND)
            this.addProperty("CPU_ABI", Build.CPU_ABI)
            this.addProperty("CPU_ABI2", Build.CPU_ABI2)
            this.addProperty("DISPLAY", Build.DISPLAY)
            this.addProperty("FINGERPRINT", Build.FINGERPRINT)
            this.addProperty("HARDWARE", Build.HARDWARE)
            this.addProperty("HOST", Build.HOST)
            this.addProperty("ID", Build.ID)
            this.addProperty("MANUFACTURER", Build.MANUFACTURER)
            this.addProperty("MODEL", Build.MODEL)
            this.addProperty("PRODUCT", Build.PRODUCT)
            this.addProperty("SERIAL", Build.SERIAL)
            this.addProperty("TAGS", Build.TAGS)
            this.addProperty("TIME", Build.TIME.toString())
            this.addProperty("TYPE", Build.TYPE)
            this.addProperty("UNKNOWN", Build.UNKNOWN)
            this.addProperty("USER", Build.USER)
            this.addProperty("UDID", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            this.addProperty("ISROOTED", "0")
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
//            binding.tvToHome -> {
//                /** REDIRECTION CODE HERE */
//            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is SplashResponse -> {

                    }
                    is AddDeviceInfoResponse -> {
                        if (apiRenderState.result.statusCode == AppConstants.Status_Code.Success) {
                            "Add Device Info API Success".logE()
                        } else {
                            "Error : Add Device Info ${apiRenderState.result.message}".logE()
                        }
                    }
                    is CheckUpdateResponse -> {
                        if (apiRenderState.result.statusCode == AppConstants.Status_Code.Success) {
                            "Check Update Response Success".logE()
                            var userDataObj = apiRenderState.result.updateData.userData
                            if (userDataObj.isForceUpdate == "0" && userDataObj.isUpdateAvailable == "0") {
                                "Nothing to Do".logE()
                            } else if (userDataObj.isForceUpdate == "0" && userDataObj.isUpdateAvailable == "1") {
                                "Show Pop up Msg for Update".logE()
                            } else if (userDataObj.isForceUpdate == "1" && userDataObj.isUpdateAvailable == "1") {
                                "Do Forcefully Update".logE()
                            } else {
                                "Something went wrong".logE()
                            }
                            //Temp Redirection For SignINACT (Please Check this)
                            this.startActivity(Intent(this, SignInAct::class.java))
                        } else {
                            "Error : Check Update Response ${apiRenderState.result.message}".logE()
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}