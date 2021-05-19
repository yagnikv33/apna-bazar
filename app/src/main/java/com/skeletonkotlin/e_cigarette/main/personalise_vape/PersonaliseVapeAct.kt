package com.skeletonkotlin.e_cigarette.main.personalise_vape

import com.skeletonkotlin.databinding.ActivityVypeBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

class PersonaliseVapeAct : BaseAct<ActivityVypeBinding, HomeVM>(Layouts.activity_personalise_vape) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}