package com.esjayit.apnabazar.main.dashboard.view.user_ledger.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class UserLedgerVM(private val repo: DashboardRepo) : BaseVM() {
    private val progressBar = MutableLiveData(false)

    //For Party Ledger Data
    fun getPartyLedgerData(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getPartyLedger(
                userId = userId,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}