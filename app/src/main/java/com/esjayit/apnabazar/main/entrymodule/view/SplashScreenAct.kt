package com.esjayit.apnabazar.main.entrymodule.view

import android.view.View
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SplashScreenAct :
    BaseAct<ActivitySplashScreenBinding, EntryVM>(Layouts.activity_splash_screen) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null
    var uuid: String = UUID.randomUUID().toString()

    override fun init() {
//        vm.getSplashScreenData()
//        vm.addDeviceInfo(uuid = Secure.getString(contentResolver, Secure.ANDROID_ID), isRooted = "0", installedId = "1")
        vm.checkForUpdate()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.tvToHome -> {
                /** REDIRECTION CODE HERE */
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.firstTime) {
          prefs.firstTime = false
        }

    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is SplashResponse -> {

                    }
                    is AddDeviceInfoResponse -> {
                        "Add Device Info Success".logE()
                    }
                    is CheckUpdateResponse -> {
                        "Check Update Response Success".logE()
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