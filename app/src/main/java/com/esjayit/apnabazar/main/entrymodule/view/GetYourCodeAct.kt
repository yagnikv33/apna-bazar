package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.OTPData
import com.esjayit.apnabazar.data.model.response.VerifyOTPResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.databinding.ActivityGetYourCodeBinding
import kotlinx.android.synthetic.main.activity_get_your_code.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class GetYourCodeAct :
    BaseAct<ActivityGetYourCodeBinding, EntryVM>(Layouts.activity_get_your_code) {

    override val vm: EntryVM? by viewModel()
    override val hasProgress: Boolean = true
    var sb = StringBuilder()
    private var otpModelObject: OTPData? = null
    private var userName: String? = null

    override fun init() {
        otpModelObject = intent.getSerializableExtra("SendOTPModel") as OTPData?
        "OTP Screen ${otpModelObject?.otpId.toString()}".logE()
        userName = intent.getStringExtra("UserName")
        setOtpEditTextHandler()
        spanResendText()
    }


    private fun setOtpEditTextHandler() {

        binding.edtOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //if(sb.isEmpty()){
                if (binding.edt1.length() == 1) {
                    sb.append(s)
                    edt_1.clearFocus()
                    edt_2.requestFocus()
                    edt_2.isCursorVisible = true
                }
                if (binding.edt2.length() == 1) {
                    // sb.append(s)
                    edt_2.clearFocus()
                    edt_3.requestFocus()
                    edt_3.isCursorVisible = true
                }
                if (binding.edt3.length() == 1) {
                    //  sb.append(s)
                    edt_3.clearFocus()
                    edt_4.requestFocus()
                    edt_4.isCursorVisible = true
                }
                if (binding.edt4.length() == 1) {
                    sb.append(s)
                    edt_4.clearFocus()
                    edt_5.requestFocus()
                    edt_5.isCursorVisible = true
                }
                if (binding.edt5.length() == 1) {
                    sb.append(s)
                    edt_5.clearFocus()
                    edt_6.requestFocus()
                    edt_6.isCursorVisible = true
                }
                if (binding.edt6.length() == 1) {
                    sb.append(s)
                    edt_6.clearFocus()
                }
                //  }
            }

            override fun afterTextChanged(s: Editable?) {
                if (sb.isEmpty()) {
                    edt_1.requestFocus()
                    edt_2.requestFocus()
                    edt_3.requestFocus()
                    edt_4.requestFocus()
                    edt_5.requestFocus()
                    edt_6.requestFocus()
                }
            }
        })

    }
//    binding.edt1.addTextChangedListener(new TextWatcher()
//    {
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            if (sb.length() == 0&edtPasscode1.length() == 1)
//            {
//                sb.append(s);
//                edtPasscode1.clearFocus();
//                edtPasscode2.requestFocus();
//                edtPasscode2.setCursorVisible(true);
//
//            }
//        }
//
//        public void beforeTextChanged(
//            CharSequence s, int start, int count,
//            int after
//        ) {
//
//            if (sb.length() == 1) {
//
//                sb.deleteCharAt(0);
//
//            }
//
//        }
//
//        public void afterTextChanged(Editable s) {
//            if (sb.length() == 0) {
//
//                edtPasscode1.requestFocus();
//            }
//
//        }
//    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.tvResend -> {
                startActivity(NewPwdAct::class.java)
            }
            binding.btnVerifyAndProceed -> {
                var otpTxt = "${binding.edt1.text}${binding.edt2.text}${binding.edt3.text}${binding.edt4.text}${binding.edt5.text}${binding.edt6.text}"
                "OTP Screen CODE ${otpTxt}".logE()
//                vm?.verifyOTP(otpId = otpModelObject?.otpId.toString(), otp = otpTxt, installedId = prefs.installId!!)
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
                "Error API CALLING API ERROR".logE()
            }
        }
    }

}