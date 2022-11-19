package com.esjayit.apnabazar.main.dashboard.view.home

import android.content.Intent
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.LoginResponse
import com.esjayit.apnabazar.data.model.response.UserProfileDetailResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.DashboardAct
import com.esjayit.apnabazar.main.dashboard.view.home.model.HomeVM
import com.esjayit.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFrag : BaseFrag<FragmentHomeBinding, HomeVM>(Layouts.fragment_home) {

    override val vm: HomeVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {
        if (prefs.user.userId.isNullOrEmpty()) {
            errorToast("Facing issue with user id")
        } else {
            if (prefs.userProfileDetail != null) {
                "already user profile fetched".logE()
            } else  {
                vm?.getUserProfile(userId = prefs.user.userId, installedId = prefs.installId!!)
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is UserProfileDetailResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "User Profile Fetched".logE()
                            prefs.userProfileDetail = apiRenderState.result
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }

}