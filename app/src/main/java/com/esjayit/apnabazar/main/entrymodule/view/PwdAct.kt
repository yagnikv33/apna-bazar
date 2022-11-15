package com.esjayit.apnabazar.main.entrymodule.view

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityPwdBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PwdAct : BaseAct<ActivityPwdBinding, EntryVM>(Layouts.activity_pwd) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}