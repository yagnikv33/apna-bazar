package com.yudiz.e_cigarette.main.brand_list

import com.yudiz.databinding.ActivityBluBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.home.model.HomeVM

class BluAct : BaseAct<ActivityBluBinding, HomeVM>(Layouts.activity_blu) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}