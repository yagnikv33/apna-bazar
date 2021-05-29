package com.yudiz.e_cigarette.main.homemodule.vaping

import com.yudiz.databinding.ActivityVapingBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM

class VapingAct : BaseAct<ActivityVapingBinding, HomeVM>(Layouts.activity_vaping) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}