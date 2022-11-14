package com.esjayit.apnabazar.main.entrymodule.view

import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.NewPasswordResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityNewPwdBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPwdAct : BaseAct<ActivityNewPwdBinding, EntryVM>(Layouts.activity_new_pwd) {

    override val vm: EntryVM? by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {
        //Set New Passsword API Call(Temp)
//        vm?.setNewPassword(userName = "", password = "", installedId = prefs.installId!!)
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is NewPasswordResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Redirect to password screen".logE()
                        } else {
                            "Error : New passsword Screen (Go to Login Screen) statusCode: ${apiRenderState.result.statusCode} msg: ${apiRenderState.result.message}".logE()
                            error(apiRenderState.result.message)
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