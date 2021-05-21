package com.yudiz.e_cigarette.main.categorymodule.our_brands

import com.yudiz.databinding.ActivityOurBrandsBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM

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