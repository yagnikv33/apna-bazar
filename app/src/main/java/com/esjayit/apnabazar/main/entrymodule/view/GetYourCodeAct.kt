package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.os.CountDownTimer
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.OTPData
import com.esjayit.apnabazar.data.model.response.SendOTPResponse
import com.esjayit.apnabazar.data.model.response.VerifyOTPResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityGetYourCodeBinding
import com.freedomotpview.OTPView
import org.koin.androidx.viewmodel.ext.android.viewModel

class GetYourCodeAct :
    BaseAct<ActivityGetYourCodeBinding, EntryVM>(Layouts.activity_get_your_code) {

    override val vm: EntryVM? by viewModel()
    override val hasProgress: Boolean = true
    var sb = StringBuilder()
    private var otpModelObject: OTPData? = null
    private var userName: String? = null
    private lateinit var countdownTimer: CountDownTimer
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }
    var length: Int? = 6

    override fun init() {
        otpModelObject = intent.getSerializableExtra("SendOTPModel") as OTPData?
        "OTP Screen ${otpModelObject?.otpId.toString()}".logE()
        userName = intent.getStringExtra("UserName")

        val configuration = OTPView.Builder()
            .setOTPLength(otpModelObject?.otpCount!!.toInt())
            .setHeight(80)
            .setWidth(80)
            .setHintText("X")
            .setHintColor(ContextCompat.getColor(this, R.color.grey))
            .setTextSize(22)
            .setTextColor(ContextCompat.getColor(this, R.color.black))
            .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_otp_component))
            .build()
        binding.otpView.setConfiguration(configuration)
        spanResendText()
    }

    private fun startTimer() {
        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResend.setText(millisUntilFinished.toString()).toString()
            }

            override fun onFinish() {
            }
        }.start()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.tvResend -> {
//                startActivity(NewPwdAct::class.java)
                "Username in GET CODE ${userName}"
                startTimer()
                vm?.sendOTP(userName = userName!!, installedId = prefs.installId!!)
            }
            binding.btnVerifyAndProceed -> {
                var otpTxt = binding.otpView.otp
                if (otpTxt.isNotBlank()) {
                    progressDialog?.showProgress()
                    "OTP Screen CODE ${otpTxt}".logE()
                    vm?.verifyOTP(
                        otpId = otpModelObject?.otpId.toString(),
                        otp = otpTxt,
                        installedId = prefs.installId!!
                    )
                } else {
                    errorToast("Please Enter OTP")
                }
            }
        }
    }

    private fun spanResendText() {
        val message = buildSpannedString {
            append(getString(R.string.txt_resend) + " ")
            inSpans(object : ClickableSpan() {
                override fun onClick(textView: View) {
                    //onClick
                }

                override fun updateDrawState(ds: TextPaint) {
                }
            }) {

            }
        }

        binding.tvResend.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = message
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is VerifyOTPResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Redirect to new password screen".logE()
                            val intent = Intent(this, NewPwdAct::class.java)
                            intent.putExtra("UserName", userName)
                            this.startActivity(intent)
                        } else {
                            "Error : Check User Verification statusCode: ${apiRenderState.result.statusCode} msg: ${apiRenderState.result.message}".logE()
                            error(apiRenderState.result.message)
                        }
                    }

                    is SendOTPResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "DATA SET USING RESEND OTP ${apiRenderState.result.data}".logE()
                            otpModelObject = apiRenderState.result.data
                            "DATA SET USING RESEND OTP MODEL ${otpModelObject}".logE()
                            length = apiRenderState.result.data.otpCount.toInt()
                            val configuration = OTPView.Builder()
                                .setOTPLength(length!!)
                                .setHeight(80)
                                .setWidth(80)
                                .setHintText("X")
                                .setHintColor(ContextCompat.getColor(this, R.color.gray))
                                .setTextSize(22)
                                .setTextColor(ContextCompat.getColor(this, R.color.black))
                                .setBackground(
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.bg_otp_component
                                    )
                                )
                                .build()
                            binding.otpView.setConfiguration(configuration)

                        } else if (statusCode == AppConstants.Status_Code.Code2) {
                            "Error : Send OTP ${apiRenderState.result.message}".logE()
                        } else {
                            "Error : Send OTP ${apiRenderState.result.message}".logE()
                        }

                    }
                }
            }
            ApiRenderState.Idle -> {
                progressDialog.hideProgress()
            }
            ApiRenderState.Loading -> {
                progressDialog.showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
                progressDialog.hideProgress()
            }
            is ApiRenderState.ApiError<*> -> {
                progressDialog.hideProgress()
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}
