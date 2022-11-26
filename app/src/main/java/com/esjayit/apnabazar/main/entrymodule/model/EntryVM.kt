package com.esjayit.apnabazar.main.entrymodule.model

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


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

    //For Error Logs
    fun logErrorAPI(uuid: String, errorLog: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.logErrorAdd(
                uuid = uuid,
                logError = errorLog,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    object RootUtil {
        val isDeviceRooted: Boolean
            get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

        private fun checkRootMethod1(): Boolean {
            val buildTags = Build.TAGS
            return buildTags != null && buildTags.contains("test-keys")
        }

        private fun checkRootMethod2(): Boolean {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
            )
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

        private fun checkRootMethod3(): Boolean {
            var process: Process? = null
            return try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val `in` = BufferedReader(InputStreamReader(process.inputStream))
                if (`in`.readLine() != null) true else false
            } catch (t: Throwable) {
                false
            } finally {
                process?.destroy()
            }
        }
    }
}