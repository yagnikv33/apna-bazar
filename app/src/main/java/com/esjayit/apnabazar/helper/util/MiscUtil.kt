package com.esjayit.apnabazar.helper.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object MiscUtil {
}

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}





