package com.yudiz.e_cigarette.main.entrymodule.view

import android.graphics.Color
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.*
import com.yudiz.e_cigarette.Colors
import com.yudiz.e_cigarette.Layouts
import com.yudiz.databinding.MainActBinding
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.entrymodule.model.MainActVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainAct : BaseAct<MainActBinding, MainActVM>(Layouts.act_main/*, FragFactory()*/) {

    override val vm: MainActVM by viewModel()
    override val hasProgress: Boolean = true

    override fun init() {

        delayedExecutor(5000) {
            successToast("delayed toast")
        }

        binding.url =
            "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"

        binding.tv.text = buildSpannedString {
            bold { append("hi") }
            underline { append(" how are you ") }
            strikeThrough { append(" 2 ") }
            color(ContextCompat.getColor(this@MainAct, Colors.colorAccent)) { append(" nice ") }
            inSpans(object : ClickableSpan() {
                override fun onClick(textView: View) {
                    errorToast("asdasd")
                }

                override fun updateDrawState(ds: TextPaint) {
//                    ds.isUnderlineText = false
                }
            }) { bold { append(" !!!!!") } }
        }

        binding.tv.highlightColor = Color.TRANSPARENT
        /**
         * added for click to work
         */
        binding.tv.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.tv.id ->
                vm.login("a", "a")
//                mediaPicker.openGallery(this)
            binding.btFrag.id -> {
//                addFrag<DemoFrag>(R.id.frame)
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.Loading -> showProgress()
            is ApiRenderState.ApiSuccess<*> -> {
                hideProgress()
                successToast("Success")
            }
            is ApiRenderState.ValidationError -> {
                apiRenderState.message?.let {
                    errorToast(getString(it)) { isDismissed ->
                        Toast.makeText(this, "dismissed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}