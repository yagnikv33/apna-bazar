package com.yudiz.e_cigarette.main.saving_calc

import com.yudiz.databinding.ActivitySavingCalcBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.home.model.HomeVM

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