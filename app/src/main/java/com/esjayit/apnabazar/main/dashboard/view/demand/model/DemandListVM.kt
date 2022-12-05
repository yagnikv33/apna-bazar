package com.esjayit.apnabazar.main.dashboard.view.demand.model

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo
import kotlinx.coroutines.launch

class DemandListVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    val subjectData = mutableListOf<ItemlistItem?>()
    val editDemandData = mutableListOf<DemandItemslistItem?>()
    var demandList = mutableListOf<DemandListItem?>()
    var viewDemandList = mutableListOf<ViewDemandItemslistItem?>()
    var btnEnable = ObservableField(true)

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
    ) {
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

    fun getDemandList(
        userid: String,
        installId: String
    ) {
        try {
            viewModelScope.launch {
                /*scope {
                }*/
                progressBar.postValue(true)
                state.emit(ApiRenderState.Loading)
                repo.getDemandList(
                    userId = userid,
                    installId = installId,
                    onError = onApiError
                ).let {
                    state.emit(ApiRenderState.ApiSuccess(it))
                    progressBar.postValue(false)
                }
            }
        } catch (e: Exception) {
            e.message.toString().logE()
        }
    }

    fun addDemandString(
        demandData: AddDemand
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addDemandString(
                demandData,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    fun getEditDemandData(
        userid: String,
        demandid: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getEditDemandData(
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

    //Edit Demand API Call
    fun editDemand(
        itemslist: Array<DummyEditDemand>,
        demandid: String,
        demanddate: String,
        userid: String,
        totalamt: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.editDemand(
                itemList = itemslist,
                demandid = demandid,
                userid = userid,
                totalamt = totalamt,
                installid = installid,
                demanddate = demanddate,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    fun addEditDemand(
        data: EditDemandDataVal
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.editDemandData(
                data = data,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }


    fun getSingleEditItemDetail(
        userid: String,
        itemid: String,
        installid: String,
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getSingleEditItemDetail(
                userid = userid,
                itemid = itemid,
                installid = installid,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}