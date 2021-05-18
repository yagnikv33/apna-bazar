package com.skeletonkotlin.e_cigarette.main.our_brands

import com.skeletonkotlin.databinding.ActivityOurBrandsBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

class OurBrandsAct : BaseAct<ActivityOurBrandsBinding, HomeVM>(Layouts.activity_our_brands) {

    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}