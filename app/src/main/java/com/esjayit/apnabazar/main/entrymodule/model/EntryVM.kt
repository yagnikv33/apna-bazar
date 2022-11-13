package com.esjayit.apnabazar.main.entrymodule.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.SplashResponse
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo

class EntryVM(private val repo: EntryRepo) : BaseVM() {

    var splashData = MutableLiveData(SplashResponse())
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

    fun addDeviceInfo(version_release: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addDeviceInfo(
                version_relese = version_release,
                onApiError
            ).let {
//                splashData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}