package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.CheckUserVerificationResponse
import com.esjayit.apnabazar.data.model.response.SendOTPResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivitySignInBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInAct : BaseAct<ActivitySignInBinding, EntryVM>(Layouts.activity_sign_in) {

    override val vm: EntryVM by viewModel()
    override val hasProgress: Boolean = true
    var userName: String = ""
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    override fun init() {
//        addListeners()
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
                binding.editText.hideSoftKeyboard()
                // errorToast("Login Btn Tapped")
                "Login Button Tapped".logE()
                //TEMP API CALL
                // userName = "ESJAYIT"
                // vm.checkUserVerification(userName = binding.editText.text.toString(), installedId = prefs.installId!!)
                userName = binding.editText.text.toString()
                if (userName.isNotEmpty()) {
                    progressDialog?.showProgress()
                    vm.checkUserVerification(userName = userName, installedId = prefs.installId!!)
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
                        progressDialog?.hideProgress()
                        when (apiRenderState.result.statusCode) {
                            AppConstants.Status_Code.Success -> {
                                "Go to Password Screen".logE()
                                val intent = Intent(this, PwdAct::class.java)
                                intent.putExtra("UserName", userName)
                                this.startActivity(intent)
                            }
                            AppConstants.Status_Code.Code3 -> {
                                "Send OTP Task and Go to OTP Screen for Verification Check User Verification ${apiRenderState.result.message}".logE()
                                vm.sendOTP(userName = userName, installedId = prefs.installId!!)
                            }
//                            AppConstants.Status_Code.Code2 -> {
//                                error(apiRenderState.result.message)
//                                //"Error : Check User Verification ${apiRenderState.result.message}".logE()
//                            }
                            else -> {
                                errorToast(apiRenderState.result.message)
                                //"Error : Check User Verification ${apiRenderState.result.message}".logE()
                            }
                        }
                    }

                    is SendOTPResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            val intent = Intent(this, GetYourCodeAct::class.java)
                            vm.otpModel.set(apiRenderState.result.data.otpCount.toInt())
                            intent.putExtra("SendOTPModel", apiRenderState.result.data)
                            intent.putExtra("UserName", userName)
                            this.startActivity(intent)
                        } else if (statusCode == AppConstants.Status_Code.Code2) {
                            "Error : Send OTP ${apiRenderState.result.message}".logE()
                        } else {
                            "Error : Send OTP ${apiRenderState.result.message}".logE()
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
                progressDialog?.showProgress()
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}