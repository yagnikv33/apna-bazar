package com.esjayit.apnabazar.main.dashboard.view.user_ledger

import android.R.string
import android.os.Environment
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.PartyLedgerResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.model.UserLedgerVM
import com.esjayit.databinding.FragmentUserLedgerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class UserLedgerFrag :
    BaseFrag<FragmentUserLedgerBinding, UserLedgerVM>(Layouts.fragment_user_ledger) {

    override val hasProgress: Boolean = false
    override val vm: UserLedgerVM by viewModel()
    val progressDialog: CustomProgress by lazy { CustomProgress(requireActivity()) }

    override fun init() {
        vm?.getPartyLedgerData(userId = prefs.user.userId, installedId = prefs.installId!!)

    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is PartyLedgerResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "USER LEDGER API ${apiRenderState.result.data}".logE()
                            successToast(apiRenderState.result.data.toString())
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                        }
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