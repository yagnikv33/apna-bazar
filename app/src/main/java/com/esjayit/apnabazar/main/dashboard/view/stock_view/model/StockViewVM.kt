package com.esjayit.apnabazar.main.dashboard.view.stock_view.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.GetReturnLisitngResponse
import com.esjayit.apnabazar.data.model.response.RetunlistItem
import com.esjayit.apnabazar.data.model.response.ReturnitemsItem
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class StockViewVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    var returnListData = MutableLiveData(GetReturnLisitngResponse())
    var returnList = mutableListOf<RetunlistItem?>()

    //For Return Listing Data
    fun getReturnListData(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnListing(
                userId = userId,
                installId = installedId,
                onApiError).let {
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
    fun addReturnBook(
        returnList: Array<ReturnitemsItem>,
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