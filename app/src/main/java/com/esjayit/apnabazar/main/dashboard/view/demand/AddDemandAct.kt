package com.esjayit.apnabazar.main.dashboard.view.demand

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityAddDemandBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddDemandAct : BaseAct<ActivityAddDemandBinding, DemandListVM>(Layouts.activity_add_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}