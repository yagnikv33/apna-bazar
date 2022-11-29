package com.esjayit.apnabazar.main.dashboard.view.stock_view.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class StockViewVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    var returnListData = MutableLiveData(GetReturnLisitngResponse())
    var returnList = mutableListOf<RetunlistItem?>()
    var returnDataList = mutableListOf<ReturnitemsItem?>()
    var viewReturnList = mutableListOf<RetutranlistItem>()

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

    //For Return Listing Data
    fun getReturnListData(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnListing(
                userId = userId,
                installId = installedId,
                onApiError
            ).let {
                returnListData.postValue(it)
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
    fun addReturnBookDataVal(
        addReturnBook: AddReturnBook
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.addReturnBookData(
                addReturnBook = addReturnBook,
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