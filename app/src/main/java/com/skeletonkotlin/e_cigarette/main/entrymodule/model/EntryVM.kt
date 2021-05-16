package com.skeletonkotlin.e_cigarette.main.entrymodule.model

import androidx.lifecycle.MutableLiveData
import com.skeletonkotlin.e_cigarette.data.model.response.SplashResponse
import com.skeletonkotlin.e_cigarette.main.base.BaseVM
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.entrymodule.repo.EntryRepo

class EntryVM(private val repo: EntryRepo) : BaseVM() {

    var splashData = MutableLiveData(SplashResponse())
    val progressBar = MutableLiveData(false)

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
}