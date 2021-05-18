package com.skeletonkotlin.e_cigarette.main.home

import android.content.Intent
import android.view.View
import com.skeletonkotlin.BR
import com.skeletonkotlin.databinding.ActivityHomeBinding
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.AQUA_VAPE
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.BLU
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.JUUL
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.LOGIC
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.TOTALLY_WICKED
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.VAMPIRE_VAPE
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.VAPOURIZE
import com.skeletonkotlin.e_cigarette.AppConstants.App.Brands.VYPE
import com.skeletonkotlin.e_cigarette.AppConstants.App.Buttons.OUR_BRANDS
import com.skeletonkotlin.e_cigarette.AppConstants.App.Buttons.PERSONALISE_VAPE
import com.skeletonkotlin.e_cigarette.AppConstants.App.Buttons.SAVING_CALCULATOR
import com.skeletonkotlin.e_cigarette.AppConstants.App.Buttons.TESTIMONIALS
import com.skeletonkotlin.e_cigarette.AppConstants.App.Buttons.WHAT_IS_VAPING
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.data.model.response.BrandsItem
import com.skeletonkotlin.e_cigarette.data.model.response.ButtonsItem
import com.skeletonkotlin.e_cigarette.data.model.response.HomeResponse
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.skeletonkotlin.e_cigarette.main.brand_list.BluAct
import com.skeletonkotlin.e_cigarette.main.brand_list.LogicAct
import com.skeletonkotlin.e_cigarette.main.brand_list.VampireVapeAct
import com.skeletonkotlin.e_cigarette.main.brand_list.VypeAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM
import com.skeletonkotlin.e_cigarette.main.our_brands.OurBrandsAct
import com.skeletonkotlin.e_cigarette.main.personalise_vape.PersonaliseVapeAct
import com.skeletonkotlin.e_cigarette.main.saving_calc.SavingCalcAct
import com.skeletonkotlin.e_cigarette.main.testimonials.TestimonialsAct
import com.skeletonkotlin.e_cigarette.main.vaping.VapingAct
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeAct : BaseAct<ActivityHomeBinding, HomeVM>(Layouts.activity_home) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private lateinit var buttonAdapter: BaseRvBindingAdapter<ButtonsItem>
    private lateinit var brandAdapter: BaseRvBindingAdapter<BrandsItem>

    override fun init() {
        vm.getPortalData()
        initButtonRecyclerView()
        initBrandRecyclerView()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                finish()
            }
        }
    }

    private fun initButtonRecyclerView() {
        buttonAdapter = BaseRvBindingAdapter(
            Layouts.raw_buttons,
            mutableListOf(),
            clickListener = ::rvButtonClickListener,
            br = BR.data
        )
        binding.rvButtonList.adapter = buttonAdapter
    }

    private fun initBrandRecyclerView() {
        brandAdapter = BaseRvBindingAdapter(
            Layouts.raw_brand_list,
            mutableListOf(),
            clickListener = ::rvBrandClickListener,
            br = BR.data
        )
        binding.rvBrandList.adapter = brandAdapter
    }

    private fun rvButtonClickListener(v: View, item: ButtonsItem, pos: Int) {
        vm.portalData.value?.id = item.id
        onButtonClick(vm.portalData.value?.id)
    }

    private fun onButtonClick(id: String?) {
        when (id) {
            PERSONALISE_VAPE -> {
                startActivity(Intent(this, PersonaliseVapeAct::class.java))
            }
            OUR_BRANDS -> {
                startActivity(Intent(this, OurBrandsAct::class.java))
            }
            WHAT_IS_VAPING -> {
                startActivity(Intent(this, VapingAct::class.java))
            }
            SAVING_CALCULATOR -> {
                startActivity(Intent(this, SavingCalcAct::class.java))
            }
            TESTIMONIALS -> {
                startActivity(Intent(this, TestimonialsAct::class.java))
            }
        }
    }

    private fun rvBrandClickListener(v: View, item: BrandsItem, pos: Int) {
        vm.portalData.value?.id = item.id
        onBrandClick(vm.portalData.value?.id)
    }

    private fun onBrandClick(id: String?) {
        when (id) {
            VAMPIRE_VAPE -> {
                startActivity(Intent(this, VampireVapeAct::class.java))
            }
            LOGIC -> {
                startActivity(Intent(this, LogicAct::class.java))
            }
            BLU -> {
                startActivity(Intent(this, BluAct::class.java))
            }
            VYPE -> {
                startActivity(Intent(this, VypeAct::class.java))
            }
            TOTALLY_WICKED -> {
            }
            JUUL -> {
            }
            AQUA_VAPE -> {
            }
            VAPOURIZE -> {
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is HomeResponse) {
                    apiRenderState.result.data?.buttons?.let {
                        buttonAdapter.addData(
                            it,
                            isClear = true
                        )
                    }
                    apiRenderState.result.data?.brands?.let {
                        brandAdapter.addData(
                            it,
                            isClear = true
                        )
                    }
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