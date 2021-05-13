package com.skeletonkotlin.e_cigarette.main.entrymodule.state

import com.skeletonkotlin.e_cigarette.data.model.response.LoginResModel

sealed class MainActState {
    object Idle : MainActState()
    object Loading : MainActState()
    object ValidationError : MainActState()
    data class ApiSuccess(val loginResModel: LoginResModel) : MainActState()
}