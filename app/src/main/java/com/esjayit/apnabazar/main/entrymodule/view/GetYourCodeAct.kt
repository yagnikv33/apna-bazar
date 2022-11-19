package com.esjayit.apnabazar.main.entrymodule.view

import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.OTPData
import com.esjayit.apnabazar.data.model.response.VerifyOTPResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
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
    private lateinit var countdownTimer: CountDownTimer
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    override fun init() {
        otpModelObject = intent.getSerializableExtra("SendOTPModel") as OTPData?
        "OTP Screen ${otpModelObject?.otpId.toString()}".logE()
        userName = intent.getStringExtra("UserName")
        setOtpEditTextHandler()
        spanResendText()

        //GenericTextWatcher here works only for moving to next EditText when a number is entered
//first parameter is the current EditText and second parameter is next EditText
        edt_1.addTextChangedListener(GenericTextWatcher(edt_1, edt_2))
        edt_2.addTextChangedListener(GenericTextWatcher(edt_2, edt_3))
        edt_3.addTextChangedListener(GenericTextWatcher(edt_3, edt_4))
        edt_4.addTextChangedListener(GenericTextWatcher(edt_4, edt_5))
        edt_5.addTextChangedListener(GenericTextWatcher(edt_5, edt_6))

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        edt_1.setOnKeyListener(GenericKeyEvent(edt_1, null))
        edt_2.setOnKeyListener(GenericKeyEvent(edt_2, edt_1))
        edt_3.setOnKeyListener(GenericKeyEvent(edt_3, edt_2))
        edt_4.setOnKeyListener(GenericKeyEvent(edt_4,edt_3))
        edt_5.setOnKeyListener(GenericKeyEvent(edt_5, edt_4))
        edt_6.setOnKeyListener(GenericKeyEvent(edt_6,edt_5))
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
                var otpTxt = "${binding.edt1.text}${binding.edt2.text}${binding.edt3.text}${binding.edt4.text}${binding.edt5.text}${binding.edt6.text}"
                if (otpTxt.isNotBlank()) {
                    progressDialog?.showProgress()
                    "OTP Screen CODE ${otpTxt}".logE()
                    vm?.verifyOTP(otpId = otpModelObject?.otpId.toString(), otp = otpTxt, installedId = prefs.installId!!)
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
class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.edt_1 && !currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView!!.requestFocus()
            return true
        }
        return false
    }
}

class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView.id) {
            R.id.edt_1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_3 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_4 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_5 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_6 -> if (text.length == 1) nextView!!.requestFocus()
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }
}