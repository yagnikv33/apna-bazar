package com.yudiz.e_cigarette.main.homemodule.model

import androidx.lifecycle.MutableLiveData
import com.yudiz.e_cigarette.data.model.response.BrandItemResponse
import com.yudiz.e_cigarette.data.model.response.HomeResponse
import com.yudiz.e_cigarette.main.base.BaseVM
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.repo.HomeRepo

class HomeVM(private val repo: HomeRepo) : BaseVM() {

    var portalData = MutableLiveData(HomeResponse())
    var brandData = MutableLiveData(BrandItemResponse())

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

    fun getBrandData(id: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getBrandData(
                id,
                onApiError
            ).let {
                brandData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }
}