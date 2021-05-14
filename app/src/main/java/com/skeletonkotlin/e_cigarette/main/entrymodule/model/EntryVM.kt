package com.skeletonkotlin.e_cigarette.main.entrymodule.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.skeletonkotlin.e_cigarette.AppConstants.Api.Value.COMPANY_ID
import com.skeletonkotlin.e_cigarette.data.model.response.SplashResponse
import com.skeletonkotlin.e_cigarette.main.base.BaseVM
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.entrymodule.repo.EntryRepo
import org.koin.core.KoinComponent

class EntryVM(private val repo: EntryRepo) : BaseVM(), KoinComponent {

    var splashData = MutableLiveData(SplashResponse())

    fun getSplashScreenData() {
        scope {
            state.emit(ApiRenderState.Loading)
            repo.getSplashData(
                COMPANY_ID,
                onApiError
            ).let {
                splashData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
            }
        }
    }
}