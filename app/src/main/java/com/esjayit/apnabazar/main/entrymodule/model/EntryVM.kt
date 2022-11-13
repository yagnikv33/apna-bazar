package com.esjayit.apnabazar.main.entrymodule.model

import android.content.Context
import android.os.Build
import android.provider.Settings.Secure
import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.AddDeviceInfoResponse
import com.esjayit.apnabazar.data.model.response.CheckUpdateResponse
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo
import java.io.File


class EntryVM(private val repo: EntryRepo) : BaseVM() {

    var splashData = MutableLiveData(SplashResponse())
    var addDeviceData = MutableLiveData(AddDeviceInfoResponse())
    var checkUpdateData = MutableLiveData(CheckUpdateResponse())

    private val progressBar = MutableLiveData(false)

    fun getSplashScreenData() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getSplashData(
                onApiError
            ).let {
                splashData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

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

    fun checkForUpdate() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.checkUpdate {  }.let {
                checkUpdateData.postValue(it)
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