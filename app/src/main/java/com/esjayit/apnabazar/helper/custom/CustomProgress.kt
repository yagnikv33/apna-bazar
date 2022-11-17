package com.esjayit.apnabazar.helper.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.esjayit.R


class CustomProgress(con: Context) : Dialog(con) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.progress)
    }

    fun showProgress() {
        if (!isShowing)
            show()
    }

    fun hideProgress() {
        if (isShowing)
            dismiss()
    }
}