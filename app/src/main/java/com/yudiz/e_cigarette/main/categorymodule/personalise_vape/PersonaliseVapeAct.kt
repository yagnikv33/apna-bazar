package com.yudiz.e_cigarette.main.categorymodule.personalise_vape
import com.yudiz.databinding.ActivityPersonaliseVapeBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM

class PersonaliseVapeAct : BaseAct<ActivityPersonaliseVapeBinding, HomeVM>(Layouts.activity_personalise_vape) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}