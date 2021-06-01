package com.yudiz.e_cigarette.main.homemodule.our_brands

import android.os.Bundle
import android.view.View
import com.yudiz.BR
import com.yudiz.databinding.ActivityOurBrandsBinding
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.BrandListData
import com.yudiz.e_cigarette.data.model.response.OurBrandsResponse
import com.yudiz.e_cigarette.helper.util.logD
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.brand_listmodule.BrandsDetailAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class OurBrandsAct : BaseAct<ActivityOurBrandsBinding, HomeVM>(Layouts.activity_our_brands) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private lateinit var brandListAdapter: BaseRvBindingAdapter<BrandListData>

    override fun init() {
        vm.getBrandList()
        setAdapter()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.cardHome -> {
                finish()
            }
            binding.cardPrevious -> {
                finish()
            }
        }
    }

    private fun rvBrandClickListener(v: View, item: BrandListData, pos: Int) {
        val idBundle = Bundle().apply {
            this.putSerializable(AppConstants.Communication.BundleData.BRAND_ITEM_ID, item.id)
        }
        startActivity(BrandsDetailAct::class.java, bundle = idBundle, null, true)
    }

    private fun setAdapter() {
        brandListAdapter = BaseRvBindingAdapter(
            Layouts.raw_our_brand,
            mutableListOf(),
            clickListener = ::rvBrandClickListener,
            br = BR.data
        )
        binding.rvOurBrand.adapter = brandListAdapter
    }

    private fun showViews() {
        binding.apply {
            tvBrand.visibility = View.VISIBLE
            cardHome.visibility = View.VISIBLE
            cardPrevious.visibility = View.VISIBLE
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is OurBrandsResponse) {
                    apiRenderState.result.data?.let {
                        brandListAdapter.addData(
                            it,
                            isClear = true
                        )
                    }
                    showViews()
                    hideProgress()
                }
            }
            ApiRenderState.Idle -> {
               hideProgress()
            }
            ApiRenderState.Loading -> {
                 showProgress()
            }
            is ApiRenderState.ValidationError -> {
            }
            is ApiRenderState.ApiError<*> -> {
                 hideProgress()
            }
        }
    }
}