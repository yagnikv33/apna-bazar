package com.skeletonkotlin.e_cigarette.main.home.model

import androidx.lifecycle.MutableLiveData
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import com.skeletonkotlin.e_cigarette.main.base.BaseVM
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.repo.HomeRepo

class HomeVM(private val repo: HomeRepo) : BaseVM() {

    var portalData = MutableLiveData(HomeResponse())


}