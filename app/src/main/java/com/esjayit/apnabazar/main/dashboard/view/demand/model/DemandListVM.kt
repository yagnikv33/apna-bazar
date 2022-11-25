package com.esjayit.apnabazar.main.dashboard.view.demand.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class DemandListVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    val subjectData = mutableListOf<ItemlistItem?>()
    var demandList = mutableListOf<DemandListItem?>()

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
        scope {
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
    }

    fun addDemand(
        itemslist: Array<DummyAddDemand>,
        demanddate: String,
        userid: String,
        totalamt: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addDemand(
                itemList = itemslist,
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
        itemslist: Array<DummyAddDemand>,
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

    //Get 5% Return Item Listing
    fun getReturnItemListing(
        userid: String,
        userMedium: String,
        installid: String,
        standard: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnItemListing(
                userId = userid,
                installId = installid,
                userMedium = userMedium,
                standard = standard,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //Get 5% Return Single Item Details
    fun getReturnSingleItem(
        userid: String,
        installid: String,
        itemId: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnSingleDetail(
                userId = userid,
                installId = installid,
                itemId = itemId,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //Add Return API Call
    fun addReturnBook(
        returnList: Array<Returnitems>,
        billAmount: String,
        billDate: String,
        userid: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addReturnBook(
                returnList = returnList,
                billDate = billDate,
                userid = userid,
                billAmount = billAmount,
                installid = installid,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
    
    //Add Return API Call
    fun viewReturnBook(
        returnid: String,
        userid: String,
        installid: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnViewData(
                returnid = returnid,
                userid = userid,
                installid = installid,
                onError = onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

}