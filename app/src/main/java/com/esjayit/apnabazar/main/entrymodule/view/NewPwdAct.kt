package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.NewPasswordResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityNewPwdBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPwdAct : BaseAct<ActivityNewPwdBinding, EntryVM>(Layouts.activity_new_pwd) {

    override val vm: EntryVM? by viewModel()
    override val hasProgress: Boolean = false
    private var userName: String? = null
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    override fun init() {
        //Set New Passsword API Call(Temp)
//        vm?.setNewPassword(userName = "", password = "", installedId = prefs.installId!!)
        userName = intent.getStringExtra("UserName")
    }

    fun isVaildForAPI(): Boolean {
        var msg = ""
        if (binding.edtPwd.text.isNullOrEmpty()) {
            msg = "Please enter password"
        } else if (binding.edtConfirmPwd.text.isNullOrEmpty()) {
            msg = "Please enter confirm password"
        } else if (!binding.edtPwd.text.toString().equals(binding.edtConfirmPwd.text.toString())) {
            msg = "Password and confirm password should be same"
        }

        return if(msg.isEmpty()) { true } else {
            errorToast(msg)
            false
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnPwdSubmit -> {
                "Password Submit Button Tapped".logE()
                if (isVaildForAPI()) {
                    progressDialog?.showProgress()
                    vm?.setNewPassword(userName = userName!!, password = binding.edtPwd.text.toString(), installedId = prefs.installId!!)
                } else {
                    "Issue with API".logE()
                }
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is NewPasswordResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Redirect to password screen".logE()
                            successToast(apiRenderState.result.message)
                            val intent = Intent(this, SignInAct::class.java)
                            intent.putExtra("UserName", userName)
                            this.startActivity(intent)
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