package com.yudiz.e_cigarette.helper.util

import android.content.Context
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat

object MiscUtil {
}

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}





