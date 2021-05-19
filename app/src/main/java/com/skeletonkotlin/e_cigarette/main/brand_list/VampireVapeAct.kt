package com.skeletonkotlin.e_cigarette.main.brand_list

import com.skeletonkotlin.databinding.ActivityVampireVapeBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

class VampireVapeAct : BaseAct<ActivityVampireVapeBinding, HomeVM>(Layouts.activity_vampire_vape) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {
    }
    override fun renderState(apiRenderState: ApiRenderState) {
    }
}