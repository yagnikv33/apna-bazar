package com.esjayit.apnabazar.main.entrymodule.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.CheckUserVerificationResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivitySignInBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInAct : BaseAct<ActivitySignInBinding, EntryVM>(Layouts.activity_sign_in) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = false
    var userName: String = ""

    override fun init() {
        addListeners()
    }

    fun addListeners() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != null) {
//                    userName = s
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnLogin -> {
                "Login Button Tapped".logE()
                userName = binding.editText.text.toString()
                vm.checkUserVerification(userName = userName, installedId = prefs.installId!!)
                if (userName.isNotEmpty() || userName.isNotBlank()) {
                } else {
                    errorToast("Please enter username first")
                }
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is CheckUserVerificationResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Go to Password Screen".logE()
                        } else if (statusCode == AppConstants.Status_Code.Code3) {
                            "Send OTP Task and Go to OTP Screen for Verification Check User Verification ${apiRenderState.result.message}".logE()
                        } else if (statusCode == AppConstants.Status_Code.Code2) {
                            "Error : Check User Verification ${apiRenderState.result.message}".logE()
                        } else {
                            "Error : Check User Verification ${apiRenderState.result.message}".logE()
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