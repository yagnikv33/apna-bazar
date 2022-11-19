package com.esjayit.apnabazar.main.dashboard.view.home.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class HomeVM(private val repo: DashboardRepo) : BaseVM() {
    private val progressBar = MutableLiveData(false)

    //For User Detail Profile
    fun getUserProfile(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getUserProfile(
                userId = userId,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

}