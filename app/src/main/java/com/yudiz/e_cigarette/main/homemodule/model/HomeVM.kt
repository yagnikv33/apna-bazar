package com.yudiz.e_cigarette.main.homemodule.model

import androidx.lifecycle.MutableLiveData
import com.yudiz.e_cigarette.data.model.response.*
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseVM
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.repo.HomeRepo

class HomeVM(private val repo: HomeRepo) : BaseVM() {

    var portalData = MutableLiveData(HomeResponse())
    var brandData = MutableLiveData(BrandItemResponse())
    var brandList = MutableLiveData(OurBrandsResponse())
    var vapingData = MutableLiveData(VapingResponse())
    var factsData = MutableLiveData(KnowFactsResponse())
    var pgVgData = MutableLiveData(PgVgResponse())

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

    fun getBrandList() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getBrandList {
                onApiError
            }.let {
                brandList.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }

    fun getVapingData() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getVapingList {
                onApiError
            }.let {
                vapingData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }

    fun getFactsList() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getFactsList {
                onApiError
            }.let {
                factsData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }

    fun getPgVgData() {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getPgVgData {
                onApiError
            }.let {
                pgVgData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(true)
            }
        }
    }
}