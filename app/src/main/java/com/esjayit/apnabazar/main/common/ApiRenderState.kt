package com.esjayit.apnabazar.main.common

import androidx.annotation.StringRes

sealed class ApiRenderState {
    object Idle : ApiRenderState()
    object Loading : ApiRenderState()
    data class ValidationError(@StringRes val message: Int?) : ApiRenderState()
    data class ApiSuccess<out T>(val result: T) : ApiRenderState()
    data class ApiError<out T>(val error: T?) : ApiRenderState()
}