package com.esjayit.apnabazar.main.dashboard.view.demand.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.ItemData
import com.esjayit.apnabazar.data.model.response.ItemlistItem
import com.esjayit.apnabazar.helper.util.PrefUtil
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DemandListVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    val subjectData = mutableListOf<ItemlistItem?>()

    fun getMediumList(
        userid: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getMediumList(
                userid = userid,
                installid = installid,
                onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    fun getStandard(
        userid: String,
        userMedium: String,
        installid: String,
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getStandard(
                userid = userid,
                installid = installid,
                userMedium = userMedium,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    fun getSubjectListData(
        userid: String,
        userMedium: String,
        installid: String,
        standard: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getSubjectList(
                userid = userid,
                installid = installid,
                userMedium = userMedium,
                standard = standard,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    fun getViewDemandList(
        userid: String,
        demandid: String,
        installid: String
    ){
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getViewDemandList(
                userid = userid,
                installid = installid,
                demandid = demandid,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}