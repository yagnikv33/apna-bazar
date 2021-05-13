package com.skeletonkotlin.e_cigarette.helper.util

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object ToastUtil {

    /*top snackbar
    fun errorSnackbar(message: String, view: View?, callback: ((Boolean) -> Unit)?) {
        TopSnackbar.make(view!!, message, Snackbar.LENGTH_LONG).let {
            it.view.setBackgroundColor(Color.parseColor("#dd5a5a"))
            it.setCallback(object : TopSnackbar.Callback() {
                override fun onDismissed(snackbar: TopSnackbar, event: Int) {
                    callback?.invoke(true)
                }
            })
            it.show()
        }
    }

    fun successSnackbar(message: String, view: View?, callback: ((Boolean) -> Unit)?) {
        TopSnackbar.make(view!!, message, Snackbar.LENGTH_LONG).let {
            it.view.setBackgroundColor(Color.parseColor("#87cc6c"))
            it.setCallback(object : TopSnackbar.Callback() {
                override fun onDismissed(snackbar: TopSnackbar, event: Int) {
                    callback?.invoke(true)
                }
            })
            it.show()
        }
    }*/

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
//        (snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView).setTextColor(Color.WHITE)
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
//        (snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView).setTextColor(Color.WHITE)
            it.show()
        }
    }
}


