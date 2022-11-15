package com.esjayit.apnabazar.main.homemodule.model


import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.CheckUserActiveResponse
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.homemodule.repo.HomeRepo


class HomeVM(private val repo: HomeRepo) : BaseVM() {

    //For Home Screen API Data
    var checkUserActiveData = MutableLiveData(CheckUserActiveResponse())
    private val progressBar = MutableLiveData(false)

    //For Check User Active or Not
    fun checkUserActiveStatus(userId: String, installedId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.checkUserActive(
                userId = userId,
                installed = installedId,
                onApiError).let {
                checkUserActiveData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}