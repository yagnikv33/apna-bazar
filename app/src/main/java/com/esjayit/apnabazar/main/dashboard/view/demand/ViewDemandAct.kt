package com.esjayit.apnabazar.main.dashboard.view.demand

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.GetDemandDataResponse
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.data.model.response.StandardResponse
import com.esjayit.apnabazar.data.model.response.ViewDemandDetailsData
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityViewDemandBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewDemandAct :
    BaseAct<ActivityViewDemandBinding, DemandListVM>(Layouts.activity_view_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {
        vm.getViewDemandList(
            userid = prefs.user.userId,
            demandid = "C6EEC52F-BD47-43DE-B15D-F6FCD52259E8",
            installid = prefs.installId.orEmpty()
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is ViewDemandDetailsData -> {
                        "Response: ${apiRenderState.result.demand}".logE()
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                // showProgress()
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