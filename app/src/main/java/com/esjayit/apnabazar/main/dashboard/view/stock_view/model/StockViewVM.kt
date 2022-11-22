package com.esjayit.apnabazar.main.dashboard.view.stock_view.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class StockViewVM(private val repo: DashboardRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)

    //For Return Listing Data
    fun getReturnListData(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getReturnListing(
                userId = userId,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}