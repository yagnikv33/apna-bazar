package com.esjayit.apnabazar.main.dashboard.view.demand.model

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo

class DemandListVM(private val repo: DashboardRepo) : BaseVM() {
    private val progressBar = MutableLiveData(false)

    fun getMediumList(
        userid: String,
        installid: String,
        versioncode: String,
        packagename: String
    ) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getMediumList(
                userid = userid,
                installid = installid,
                versioncode = versioncode,
                packagename = packagename,
                onApiError
            ).let {
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}