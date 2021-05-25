package com.yudiz.e_cigarette.main.homemodule

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yudiz.BR
import com.yudiz.databinding.ActivityHomeBinding
import com.yudiz.e_cigarette.AppConstants.App.Buttons.OUR_BRANDS
import com.yudiz.e_cigarette.AppConstants.App.Buttons.PERSONALISE_VAPE
import com.yudiz.e_cigarette.AppConstants.App.Buttons.SAVING_CALCULATOR
import com.yudiz.e_cigarette.AppConstants.App.Buttons.TESTIMONIALS
import com.yudiz.e_cigarette.AppConstants.App.Buttons.WHAT_IS_VAPING
import com.yudiz.e_cigarette.AppConstants.Communication.BundleData.BRAND_ITEM_ID
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.BrandsItem
import com.yudiz.e_cigarette.data.model.response.ButtonsItem
import com.yudiz.e_cigarette.data.model.response.HomeResponse
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.brand_listmodule.BrandsDetailAct
import com.yudiz.e_cigarette.main.categorymodule.our_brands.OurBrandsAct
import com.yudiz.e_cigarette.main.categorymodule.personalise_vape.PersonaliseVapeAct
import com.yudiz.e_cigarette.main.categorymodule.saving_calc.SavingCalcAct
import com.yudiz.e_cigarette.main.categorymodule.testimonials.TestimonialsAct
import com.yudiz.e_cigarette.main.categorymodule.vaping.VapingAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
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
            params.bottomMargin = 43
            params.height = 250
            holder.itemView.layoutParams = params
        }
    }

    private fun rvBrandClickListener(v: View, item: BrandsItem, pos: Int) {

        val idBundle = Bundle().apply {
            this.putSerializable(BRAND_ITEM_ID, item.id)
        }
        startActivity(BrandsDetailAct::class.java, bundle = idBundle, null, true)
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