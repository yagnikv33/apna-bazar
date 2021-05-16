package com.skeletonkotlin.e_cigarette.main.home

import android.view.View
import com.skeletonkotlin.BR
import com.skeletonkotlin.databinding.ActivityHomeBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeAct : BaseAct<ActivityHomeBinding,HomeVM>(Layouts.activity_home) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    lateinit var adapter: BaseRvBindingAdapter<HomeResponse>

    override fun init() {
        initRecyclerView()
    }

    private fun rvClickListener(v: View, item: HomeResponse, pos: Int) {

    }

    private fun initRecyclerView() {
        adapter = BaseRvBindingAdapter(
            Layouts.raw_brand_list,
            mutableListOf(),
            clickListener = ::rvClickListener,
            br = BR._all
        )
         binding.rvBrandList.adapter = adapter
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}