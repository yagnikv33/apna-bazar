package com.esjayit.apnabazar.main.entrymodule.model

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo
import com.google.gson.JsonObject
import java.io.File


class EntryVM(private val repo: EntryRepo) : BaseVM() {

    var splashData = MutableLiveData(SplashResponse())
    //For Splash Screen Data
    var addDeviceData = MutableLiveData(AddDeviceInfoResponse())
    var checkUpdateData = MutableLiveData(CheckUpdateResponse())
    var appFirstLaunchData = MutableLiveData(AppFirstLaunchResponse())
    //For SignIn Data
    var checkUserVerificatonData = MutableLiveData(CheckUserVerificationResponse())
    var sendOTPData = MutableLiveData(SendOTPResponse())
    var verifyData = MutableLiveData(VerifyOTPResponse())
    var newPasswordData = MutableLiveData(NewPasswordResponse())
    //For Login Data
    var loginData = MutableLiveData(LoginResponse())
    //For Home Screen API Data
    var checkUserActiveData = MutableLiveData(CheckUserActiveResponse())

    private val progressBar = MutableLiveData(false)

    //For Splash Screen APIs
    //For App First Time Launch API Function
    fun appFirstTimeLaunch(fcmToken: String, playerId: String, installId: String, deviceInfoJson: JsonObject) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.appFirstLaunch(
                fcmToken = fcmToken,
                playerId = playerId,
                installId = installId,
                deviceInfoJson = deviceInfoJson,
                onApiError
            ).let {
                appFirstLaunchData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Add Device Info API
    fun addDeviceInfo(uuid: String, isRooted: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addDeviceInfo(
                uuid = uuid,
                isRooted = isRooted,
                installed = installedId,
                onApiError
            ).let {
                addDeviceData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Checking App Update
    fun checkForUpdate(installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.checkUpdate(installId = installedId, onApiError).let {
                checkUpdateData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For SignIn Screen APIs
    //For Check User Verification
    fun checkUserVerification(userName: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.checkUserVerification(
                userName = userName,
                installed = installedId,
                onApiError).let {
                checkUserVerificatonData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Send OTP
    fun sendOTP(userName: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.sendOTP(
                userName = userName,
                installed = installedId,
                onApiError).let {
                sendOTPData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Verify OTP
    fun verifyOTP(otpId: String,otp: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.verifyOTP(
                otpId = otpId,
                otp = otp,
                installed = installedId,
                onApiError).let {
                verifyData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For New Password
    fun setNewPassword(userName: String, password: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.newPassword(
                userName = userName,
                password = password,
                installed = installedId,
                onApiError).let {
                newPasswordData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Login
    fun login(userName: String, password: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.login(
                userName = userName,
                password = password,
                installed = installedId,
                onApiError).let {
                loginData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    /**
     * Checks if the device is rooted.
     *
     * @return `true` if the device is rooted, `false` otherwise.
     */
    fun isRooted(): Boolean {

        // get from build info
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
        } catch (e1: Exception) {
            // ignore
        }

        // try executing commands
        return (canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su"))
    }

    // executes a command on the system
    private fun canExecuteCommand(command: String): Boolean {
        val executedSuccesfully: Boolean
        executedSuccesfully = try {
            Runtime.getRuntime().exec(command)
            true
        } catch (e: Exception) {
            false
        }
        return executedSuccesfully
    }



}