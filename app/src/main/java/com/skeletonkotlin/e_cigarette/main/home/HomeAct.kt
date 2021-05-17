package com.skeletonkotlin.e_cigarette.main.home

import android.view.View
import com.skeletonkotlin.BR
import com.skeletonkotlin.databinding.ActivityHomeBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.data.model.response.*
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeAct : BaseAct<ActivityHomeBinding, HomeVM>(Layouts.activity_home) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    lateinit var adapter: BaseRvBindingAdapter<ButtonsItem>

    override fun init() {
        vm.getPortalData()
       // initRecyclerView()
    }

    private fun rvClickListener(v: View, item: ButtonsItem, pos: Int) {

    }

    private fun initRecyclerView() {
        adapter = BaseRvBindingAdapter(
            Layouts.raw_buttons,
            mutableListOf(),
            clickListener = ::rvClickListener,
            br = BR.data
        )
        binding.rvButtonList.adapter = adapter
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is HomeResponse) {
                    initRecyclerView()
                    apiRenderState.result.data?.buttons?.let { adapter.addData(it, isClear = true) }
                }
            }
            ApiRenderState.Idle -> {
            }
            ApiRenderState.Loading -> {
            }
            is ApiRenderState.ValidationError -> {
            }
            is ApiRenderState.ApiError<*> -> {
            }
        }
    }
}