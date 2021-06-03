package com.yudiz.e_cigarette.main.homemodule.personalise_vape

import com.yudiz.BR
import com.yudiz.databinding.ActivityPersonaliseVapeBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.*
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.json.JSONArray
import org.koin.androidx.viewmodel.ext.android.viewModel


class PersonaliseVapeAct :
    BaseAct<ActivityPersonaliseVapeBinding, HomeVM>(Layouts.activity_personalise_vape) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    private lateinit var smokingAdapter: BaseRvBindingAdapter<QuestionsItem>

    override fun init() {
        vm.getPersonaliseVapeData()
        setSmokingAdapter()
        setCigaretteAdapter()
        setBrandsAdapter()
    }

    private fun setSmokingAdapter() {
        smokingAdapter = BaseRvBindingAdapter(
            Layouts.raw_personalise_smoking_buttons,
            mutableListOf(),
            br = BR.que_item
        )
        binding.rvSmoking.adapter = smokingAdapter
    }

    private fun setCigaretteAdapter() {
        binding.rvCigarette.adapter = BaseRvBindingAdapter(
            Layouts.raw_personalise_cigarette_buttons,
            mutableListOf("", "", "", ""),
            br = BR.data
        )
    }

    private fun setBrandsAdapter() {
        binding.rvBrands.adapter = BaseRvBindingAdapter(
            Layouts.raw_personalise_brands,
            mutableListOf("", "", "", ""),
            br = BR.data
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is PersonaliseVapeResponse) {

                    "Response: ${apiRenderState.result.data.questions?.get(3)?.slug}".logE()

                    when (apiRenderState.result.data.questions?.get(3)?.slug) {
                        "years_smoking" -> {
                            apiRenderState.result.data.questions.let {
                                smokingAdapter.addData(
                                    it,
                                    isClear = true
                                )
                            }
                            "Response: ${apiRenderState.result.data.questions[2].options}".logE()
                        }
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