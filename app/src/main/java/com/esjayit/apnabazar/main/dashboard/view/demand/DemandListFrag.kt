package com.esjayit.apnabazar.main.dashboard.view.demand

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.FragmentDemandListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemandListFrag :
    BaseFrag<FragmentDemandListBinding, DemandListVM>(Layouts.fragment_demand_list) {

    override val hasProgress: Boolean = false
    override val vm: DemandListVM by viewModel()

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is MediumResponse -> {

                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}