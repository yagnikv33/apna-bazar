package com.yudiz.e_cigarette.main.homemodule.testimonials

import com.yudiz.databinding.ActivityTestimonialsBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM

class TestimonialsAct :
    BaseAct<ActivityTestimonialsBinding, HomeVM>(Layouts.activity_testimonials) {
    override val vm: HomeVM?
        get() = null

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}