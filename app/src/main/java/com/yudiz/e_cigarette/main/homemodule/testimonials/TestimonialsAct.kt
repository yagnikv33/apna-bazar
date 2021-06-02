package com.yudiz.e_cigarette.main.homemodule.testimonials

import com.yudiz.BR
import com.yudiz.databinding.ActivityTestimonialsBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestimonialsAct :
    BaseAct<ActivityTestimonialsBinding, HomeVM>(Layouts.activity_testimonials) {
    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = false

    override fun init() {
        setAdapter()
    }

    private fun setAdapter() {
        binding.rvTestimonials.adapter = BaseRvBindingAdapter(
            Layouts.raw_testimonials,
            mutableListOf("","","","",""),
            br = BR.data
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}