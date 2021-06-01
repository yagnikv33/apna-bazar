package com.yudiz.e_cigarette.main.homemodule.vaping

import com.yudiz.databinding.ActivityPgVgBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class PgVgAct : BaseAct<ActivityPgVgBinding, HomeVM>(Layouts.activity_pg_vg) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}