package com.yudiz.e_cigarette.helper.util

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object ToastUtil {

    fun errorSnackbar(message: String, view: View?, callback: ((Boolean) -> Unit)?) {

        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).let {
            it.view.setBackgroundColor(Color.parseColor("#dd5a5a"))
            it.addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {

                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    callback?.invoke(true)
                }
            })
            it.show()
        }
    }

    fun successSnackbar(message: String, view: View?, callback: ((Boolean) -> Unit)?) {

        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).let {
            it.view.setBackgroundColor(Color.parseColor("#87cc6c"))
            it.addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {

                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    callback?.invoke(true)
                }
            })
            it.show()
        }
    }
}


