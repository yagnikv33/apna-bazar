package com.esjayit.apnabazar.main.entrymodule.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.AppFirstLaunchResponse
import com.esjayit.apnabazar.data.model.response.CheckUpdateResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.DashboardAct
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivitySplashScreenBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.JsonObject
import com.onesignal.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SplashScreenAct :
    BaseAct<ActivitySplashScreenBinding, EntryVM>(Layouts.activity_splash_screen),
    OSSubscriptionObserver {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null
    var uuid: String = UUID.randomUUID().toString()
    var isRooted = if (EntryVM.RootUtil.isDeviceRooted) "1" else "0"

    override fun init() {
        OneSignal.addSubscriptionObserver(this)
        "isRooted : ${isRooted}".logE()
        "UUID ${
            Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } RandomUUID : ${uuid}".logE()
        checkForLaunchAPIs()
        "Pref: ${prefs.authToken}".logE()
    }

    override fun onResume() {
        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.intent.action.SmsReceiver")
//        registerReceiver(mServiceReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try {
            if (mServiceReceiver != null) {
                unregisterReceiver(mServiceReceiver)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //For Notification Change One Signal
    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges) {
        if (!stateChanges.from.isSubscribed &&
            stateChanges.to.isSubscribed
        ) {
            // get player ID
            if (prefs.firstTime) {
                vm.appFirstTimeLaunch(
                    fcmToken = stateChanges.to.pushToken,
                    installId = uuid,
                    playerId = stateChanges.to.userId,
                    deviceInfoJson = convertedJSONObject()
                )
            }
            prefs.playerId = stateChanges.to.userId
            prefs.pushToken = stateChanges.to.pushToken
        }
        Log.i("Debug", "onOSPermissionChanged: $stateChanges")
    }

    //For Launch App API Calling
    private fun checkForLaunchAPIs() {
        if (prefs.authToken.isNullOrEmpty()) {
            //login
            if (prefs.firstTime) {
                // First Time Launch
                "RUN : First Time".logE()

                prefs.installId = uuid

                vm.addDeviceInfo(
                    uuid = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    ), isRooted = isRooted, installedId = uuid
                )
                binding.progress?.set(true)
            } else {
                binding.progress?.set(true)
                // App is not First Time Launch
                "RUN : Not First Time".logE()
                if (TextUtils.isEmpty(prefs.installId)) {
                    "InstallId is Empty Please check it!!!!".logE()
                } else {
                    "Found InstallId ${prefs.installId!!}".logE()
                    vm.checkForUpdate(installedId = prefs.installId!!)
                }
            }
        } else {
            this.startActivity(Intent(this, DashboardAct::class.java))
            finishAffinity()
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
            this.addProperty(
                "UDID",
                Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            )
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

    fun showUpdatePopUp() {
        android.app.AlertDialog.Builder(this)
            .setMessage("Please update new version of this app")
            .setCancelable(false)
            .setPositiveButton("Update",
                DialogInterface.OnClickListener { dialog, id ->
                    "Open PlayStore"
                })
            .setNegativeButton("No", null)
            .show()
    }


    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is AddDeviceInfoResponse -> {
                        if (apiRenderState.result.statusCode == AppConstants.Status_Code.Success) {
                            "Add Device Info API Success".logE()
                        } else {
                            "Error : Add Device Info ${apiRenderState.result.message}".logE()
                        }
                    }

                    is AppFirstLaunchResponse -> {
                        if (apiRenderState.result.statusCode == AppConstants.Status_Code.Success) {
                            "First Time App Launch Success".logE()
                            prefs.firstTime = false
                            vm.checkForUpdate(installedId = uuid)
                        } else {
                            "Error : First Time App Launch ${apiRenderState.result.message}".logE()
                        }
                    }

                    is CheckUpdateResponse -> {
                        binding.progress?.set(false)
                        if (apiRenderState.result.statusCode == AppConstants.Status_Code.Success) {
                            "Check Update Response Success".logE()
                            var userDataObj = apiRenderState.result.updateData.updateDataModel
                            if (userDataObj.isForceUpdate == "0" && userDataObj.isUpdateAvailable == "0") {
                                "Nothing to Do".logE()
                            } else if (userDataObj.isForceUpdate == "0" && userDataObj.isUpdateAvailable == "1") {
                                "Show Pop up Msg for Update".logE()
                                showUpdatePopUp()
                            } else if (userDataObj.isForceUpdate == "1" && userDataObj.isUpdateAvailable == "1") {
                                "Do Forcefully Update".logE()
                            } else {
                                "Something went wrong".logE()
                            }
                            //Temp Redirection For SignINACT
                            this.startActivity(Intent(this, SignInAct::class.java))
                            finishAffinity()
                        } else {
                            "Error : Check Update Response ${apiRenderState.result.message}".logE()
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {

            }
            ApiRenderState.Loading -> {

            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }

    private val mServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            //Extract your data - better to use constants...
            val IncomingSms = intent.getStringExtra("onesingleId")
        }
    }
}