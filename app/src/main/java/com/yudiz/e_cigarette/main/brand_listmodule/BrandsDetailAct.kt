package com.yudiz.e_cigarette.main.brand_listmodule

import com.yudiz.databinding.ActivityBrandsDetailBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.BrandDetailResponse
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrandsDetailAct :
    BaseAct<ActivityBrandsDetailBinding, HomeVM>(Layouts.activity_brands_detail) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is BrandDetailResponse) {

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