package com.yudiz.e_cigarette.main.brand_list

import com.yudiz.databinding.ActivityVypeBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.home.model.HomeVM

class VypeAct : BaseAct<ActivityVypeBinding, HomeVM>(Layouts.activity_vype) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }

}