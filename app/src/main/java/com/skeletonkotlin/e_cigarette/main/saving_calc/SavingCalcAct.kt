package com.skeletonkotlin.e_cigarette.main.saving_calc

import com.skeletonkotlin.databinding.ActivitySavingCalcBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

class SavingCalcAct : BaseAct<ActivitySavingCalcBinding, HomeVM>(Layouts.activity_saving_calc) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}