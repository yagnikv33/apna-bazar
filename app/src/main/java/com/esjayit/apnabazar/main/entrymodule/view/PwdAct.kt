package com.esjayit.apnabazar.main.entrymodule.view

import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.LoginResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityPwdBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PwdAct : BaseAct<ActivityPwdBinding, EntryVM>(Layouts.activity_pwd) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = false
    private var userName: String? = null


    override fun init() {
        userName = intent.getStringExtra("UserName")
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnLogin -> {
                "Passoword Login Button Tapped ${userName.toString()} ${binding.editText.text.toString()}".logE()

                //TEMP API CALL
                vm.login(userName = userName.toString(), password = binding.editText.text.toString() ,installedId = prefs.installId!!)
//                userName = binding.editText.text.toString()
//                if (binding.editText.text?.isNotBlank() == true) {
////                    vm.login(userName = userName.toString(), password = binding.editText.text.toString() ,installedId = prefs.installId!!)
//                } else {
//                    errorToast("Please enter password")
//                }
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is LoginResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Go to Home Screen".logE()
                        } else {
                            errorToast(apiRenderState.result.message)
                            "Error : Pwd ACT ${apiRenderState.result.message}".logE()
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