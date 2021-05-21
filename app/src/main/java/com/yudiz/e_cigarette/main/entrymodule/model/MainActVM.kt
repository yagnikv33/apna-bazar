package com.yudiz.e_cigarette.main.entrymodule.model

import androidx.databinding.ObservableField
import com.yudiz.e_cigarette.Strings
import com.yudiz.e_cigarette.main.base.BaseVM
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.entrymodule.repo.MainActRepo

class MainActVM(private val repo: MainActRepo) : BaseVM() {

    val emailData = ObservableField("abc")

    fun login(email: String, password: String) {
        scope {
            if (email.isEmpty()) {
                state.emit(ApiRenderState.ValidationError(Strings.app_name))
                return@scope
            }
            state.emit(ApiRenderState.Loading)

            repo.login(email, password, onApiError)?.let {
                state.emit(ApiRenderState.ApiSuccess(it))
                return@scope
            }
        }
    }
}