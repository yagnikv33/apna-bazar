package com.skeletonkotlin.e_cigarette.main.testimonials

import com.skeletonkotlin.databinding.ActivityTestimonialsBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM

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