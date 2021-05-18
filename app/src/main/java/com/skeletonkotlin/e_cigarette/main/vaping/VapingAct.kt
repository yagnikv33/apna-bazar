package com.skeletonkotlin.e_cigarette.main.vaping

import com.skeletonkotlin.databinding.ActivityVapingBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

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