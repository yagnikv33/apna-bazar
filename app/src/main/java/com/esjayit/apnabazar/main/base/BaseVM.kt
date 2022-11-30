package com.esjayit.apnabazar.main.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esjayit.apnabazar.main.common.ApiRenderState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseVM : ViewModel(), KoinComponent {

    internal val apiError = MutableSharedFlow<ApiResult<Any>>()
    protected val onApiError: (ApiResult<Any>) -> Unit = { error ->
        viewModelScope.launch {
            apiError.emit(error)
        }
    }

    protected val state = MutableSharedFlow<ApiRenderState>()
    internal fun state(): SharedFlow<ApiRenderState> = state

    fun <T> scope(
        dispatcher: CoroutineDispatcher = IO,
        executable: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.launch(dispatcher) {
            executable.invoke(this)
        }
    }
}