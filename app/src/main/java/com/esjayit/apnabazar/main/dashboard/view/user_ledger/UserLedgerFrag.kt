package com.esjayit.apnabazar.main.dashboard.view.user_ledger

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.model.UserLedgerVM
import com.esjayit.databinding.FragmentUserLedgerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserLedgerFrag :
    BaseFrag<FragmentUserLedgerBinding, UserLedgerVM>(Layouts.fragment_user_ledger) {

    override val hasProgress: Boolean = false
    override val vm: UserLedgerVM by viewModel()

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}