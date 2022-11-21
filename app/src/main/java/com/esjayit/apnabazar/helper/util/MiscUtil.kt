package com.esjayit.apnabazar.helper.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.*

object MiscUtil {
    fun <T> throttleLatest(
        intervalMs: Long = 300L,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        executable: (T) -> Unit
    ): (T) -> Unit {
        var job: Job? = null
        var latestParam: T
        return { param: T ->
            latestParam = param
            if (job?.isCompleted != false)
                job = scope.launch(dispatcher) {
                    delay(intervalMs)
                    executable(latestParam)
                }
        }
    }
}

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )



}





