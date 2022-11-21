package com.esjayit.apnabazar.main.dashboard.view.profile.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class ProfileVM(private val repo: DashboardRepo) : BaseVM() {

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

    //For Edit Profile Detail
    fun editUserProfile(userId: String, name: String, address: String, city: String, stateStr: String, country: String,phone1: String, phone2: String, gstNo: String, panNo: String, email: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.editProfile(
                userId = userId,
                name = name,
                address = address,
                city = city,
                state = stateStr,
                country = country,
                phone1 = phone1,
                phone2 = phone2,
                gstNo = gstNo,
                panNo = panNo,
                email = email,
                installId = installedId,
                onApiError).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}