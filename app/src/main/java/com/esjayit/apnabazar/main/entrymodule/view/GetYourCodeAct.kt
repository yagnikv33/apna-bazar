package com.esjayit.apnabazar.main.entrymodule.view

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.os.CountDownTimer
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.view.size
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.OTPData
import com.esjayit.apnabazar.data.model.response.VerifyOTPResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityGetYourCodeBinding
import kotlinx.android.synthetic.main.activity_get_your_code.*
import kotlinx.android.synthetic.main.activity_get_your_code.view.*
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

    override fun init() {
        otpModelObject = intent.getSerializableExtra("SendOTPModel") as OTPData?
        "OTP Screen ${otpModelObject?.otpId.toString()}".logE()
        userName = intent.getStringExtra("UserName")
//        setOtpEditTextHandler()
//        binding.otpView.le
        spanResendText()

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                binding.otpView.hideSoftKeyboard()
            }
        }
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
