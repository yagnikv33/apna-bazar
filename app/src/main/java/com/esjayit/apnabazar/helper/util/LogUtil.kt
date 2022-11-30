package com.esjayit.apnabazar.helper.util

import android.util.Log

fun String.logE(throwable: Throwable? = null) {
    Log.e("TAG", this)
}
