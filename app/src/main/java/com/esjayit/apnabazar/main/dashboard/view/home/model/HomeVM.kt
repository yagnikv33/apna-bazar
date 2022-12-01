package com.esjayit.apnabazar.main.dashboard.view.home.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.ListItem
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class HomeVM(private val repo: DashboardRepo) : BaseVM() {
    private val progressBar = MutableLiveData(false)
    var homeListData = mutableListOf<ListItem?>()

    //For User Active Status
    fun checkUserActiveStatus(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.checkUserActive(
                userId = userId,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Home Screen Message Listing
    fun getHomeScreen(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getHomeList(
                userId = userId,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

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