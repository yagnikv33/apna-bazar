package com.esjayit.apnabazar.main.dashboard.view.home

import android.view.View
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.AddDemandAct
import com.esjayit.apnabazar.main.dashboard.view.home.model.HomeVM
import com.esjayit.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFrag : BaseFrag<FragmentHomeBinding, HomeVM>(Layouts.fragment_home) {

    override val vm: HomeVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v){
            binding.btnAddDemand -> {
                startActivity(AddDemandAct::class.java)
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }

}