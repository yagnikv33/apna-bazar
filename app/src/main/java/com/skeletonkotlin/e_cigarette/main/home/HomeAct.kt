package com.skeletonkotlin.e_cigarette.main.home

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
    private var buttonPosition = 0

    override fun init() {
        vm.getPortalData()
        initButtonRecyclerView()
        initBrandRecyclerView()
        hideViews()
    }

    private fun hideViews() {
        binding.apply {
            tvLetsGet.isVisible = false
            tvSelectYourOption.isVisible = false
            ivBack.isVisible = false
        }
    }

    private fun showViews() {
        binding.apply {
            tvLetsGet.isVisible = true
            tvSelectYourOption.isVisible = true
            ivBack.isVisible = true
        }
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
            br = BR.data,
            viewHolder = ::onBindViewHolder
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
        onButtonClick(pos)
    }

    private fun onButtonClick(pos: Int?) {
        when (pos) {
            PERSONALISE_VAPE -> {
                startActivity(PersonaliseVapeAct::class.java, null, null, shouldAnimate = true)
            }
            OUR_BRANDS -> {
                startActivity(OurBrandsAct::class.java, null, null, shouldAnimate = true)
            }
            WHAT_IS_VAPING -> {
                startActivity(VapingAct::class.java, null, null, shouldAnimate = true)
            }
            SAVING_CALCULATOR -> {
                startActivity(SavingCalcAct::class.java, null, null, shouldAnimate = true)
            }
            TESTIMONIALS -> {
                startActivity(TestimonialsAct::class.java, null, null, shouldAnimate = true)
            }
        }
    }

    private fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (pos == buttonPosition) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 30
            holder.itemView.layoutParams = params
        }
    }

    private fun rvBrandClickListener(v: View, item: BrandsItem, pos: Int) {
        vm.portalData.value?.id = item.id
        onBrandClick(pos)
    }

    private fun onBrandClick(pos: Int?) {
        when (pos) {
            VAMPIRE_VAPE -> {
                startActivity(VampireVapeAct::class.java, null, null, shouldAnimate = true)
            }
            LOGIC -> {
                startActivity(LogicAct::class.java, null, null, shouldAnimate = true)
            }
            BLU -> {
                startActivity(BluAct::class.java, null, null, shouldAnimate = true)
            }
            VYPE -> {
                startActivity(VypeAct::class.java, null, null, shouldAnimate = true)
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
                    showViews()
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