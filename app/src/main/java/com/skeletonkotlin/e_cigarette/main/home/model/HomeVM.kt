package com.skeletonkotlin.e_cigarette.main.home.model

import androidx.lifecycle.MutableLiveData
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseVM
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.repo.HomeRepo

class HomeVM(private val repo: HomeRepo) : BaseVM() {

    var portalData = MutableLiveData(HomeResponse())
    private val progressBar = MutableLiveData(false)

    fun getPortalData() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getPortalData(
                onApiError
            ).let {
                portalData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }
}