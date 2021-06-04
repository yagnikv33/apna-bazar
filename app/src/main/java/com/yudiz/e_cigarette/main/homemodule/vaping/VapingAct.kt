package com.yudiz.e_cigarette.main.homemodule.vaping

import android.content.Intent
import android.view.View
import com.yudiz.BR
import com.yudiz.databinding.ActivityVapingBinding
import com.yudiz.e_cigarette.AppConstants.App.Buttons.KNOW_THE_FACTS
import com.yudiz.e_cigarette.AppConstants.App.Buttons.PG_VS_VG
import com.yudiz.e_cigarette.AppConstants.Communication.BundleData.BRAND_ITEM_ID
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.BrandsItem
import com.yudiz.e_cigarette.data.model.response.ButtonsItem
import com.yudiz.e_cigarette.data.model.response.VapingResponse
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.brand_listmodule.BrandsDetailAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class VapingAct : BaseAct<ActivityVapingBinding, HomeVM>(Layouts.activity_vaping) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private lateinit var buttonAdapter: BaseRvBindingAdapter<ButtonsItem>
    private lateinit var brandAdapter: BaseRvBindingAdapter<BrandsItem>

    override fun init() {
        vm.getVapingData()
        setBrandAdapter()
        setButtonAdapter()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                finish()
            }
        }
    }

    private fun setBrandAdapter() {
        brandAdapter = BaseRvBindingAdapter(
            Layouts.raw_home_brand_list,
            mutableListOf(),
            clickListener = ::rvBrandClickListener,
            br = BR.data
        )
        binding.rvVapingBrands.adapter = brandAdapter
    }

    private fun rvBrandClickListener(v: View, item: BrandsItem, pos: Int) {
        startActivity(
            Intent(
                this,
                BrandsDetailAct::class.java
            ).putExtra(BRAND_ITEM_ID, item.id)
        )
    }

    private fun setButtonAdapter() {
        buttonAdapter = BaseRvBindingAdapter(
            Layouts.raw_home_buttons,
            mutableListOf(),
            clickListener = ::rvButtonClickListener,
            br = BR.data
        )
        binding.rvVapingBtn.adapter = buttonAdapter
    }

    private fun rvButtonClickListener(v: View, item: ButtonsItem, pos: Int) {
        when (item.id) {
            KNOW_THE_FACTS -> {
                startActivity(KnowTheFactsAct::class.java, null, null, shouldAnimate = true)
            }
            PG_VS_VG -> {
                startActivity(PgVgAct::class.java, null, null, shouldAnimate = true)
            }
        }
    }

    private fun setView() {
        binding.ivBack.visibility = View.VISIBLE
        binding.tvQuestion.visibility = View.VISIBLE
        binding.tvAnswer.visibility = View.VISIBLE
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is VapingResponse) {
                    apiRenderState.result.data.buttons?.let {
                        buttonAdapter.addData(
                            it,
                            isClear = true
                        )

                    }
                    apiRenderState.result.data.brands?.let {
                        brandAdapter.addData(
                            it,
                            isClear = true
                        )
                    }
                    hideProgress()
                    setView()
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