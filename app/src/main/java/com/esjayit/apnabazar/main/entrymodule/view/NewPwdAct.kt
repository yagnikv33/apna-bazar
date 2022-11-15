package com.esjayit.apnabazar.main.entrymodule.view

import android.view.View
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityNewPwdBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPwdAct : BaseAct<ActivityNewPwdBinding, EntryVM>(Layouts.activity_new_pwd) {

    override val vm: EntryVM? by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnPwdSubmit -> {
                startActivity(PwdAct::class.java)
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}