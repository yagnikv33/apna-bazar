package com.esjayit.apnabazar.main.dashboard.view.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.DashboardAct
import com.esjayit.apnabazar.main.dashboard.view.home.model.HomeVM
import com.esjayit.apnabazar.main.entrymodule.view.SignInAct
import com.esjayit.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFrag : BaseFrag<FragmentHomeBinding, HomeVM>(Layouts.fragment_home) {

    override val vm: HomeVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {
        if (prefs.user != null && prefs.installId != null) {
            vm?.checkUserActiveStatus(userId = prefs.user.userId, installedId = prefs.installId!!)
            vm?.getHomeScreen(userId = prefs.user.userId, installedId = prefs.installId!!)
        }
        if (prefs.user.userId.isNullOrEmpty()) {
            errorToast("Facing issue with user id")
        } else {
            if (prefs.userProfileDetail != null) {
                "already user profile fetched".logE()
                binding.userName.setText(prefs.userProfileDetail.userData?.detail?.uname.toString())
            } else  {
                vm?.getUserProfile(userId = prefs.user.userId, installedId = prefs.installId!!)
            }
        }
    }

    fun manageUserStatus(obj: CheckUserActiveResponse) {
        //Not Null
        val data = obj.data
        var msg = ""
        if (!data.isActive.isNullOrEmpty() && !data.isMultiDeviceAllow.isNullOrEmpty() ) {
            if (data.isActive == "1" && data.isMultiDeviceAllow == "1") {
                //Temp Show Msg
                successToast(obj.message)
            } else if (data.isActive == "1" && data.isMultiDeviceAllow == "1") {
                msg = obj.message
                showAlert(msg = msg)
            } else if (data.isActive == "0" && data.isMultiDeviceAllow == "0" || data.isActive == "0" && data.isMultiDeviceAllow == "1") {
                msg = obj.message
            }
        }
    }

    //For Show Alert
    fun showAlert(msg: String) {
        AlertDialog.Builder(requireActivity())
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Okay",
                DialogInterface.OnClickListener { dialog, id ->
                    prefs.clearPrefs()
                    this.startActivity(Intent(requireActivity(), SignInAct::class.java))})
            .show()
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is CheckUserActiveResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            manageUserStatus(apiRenderState.result)
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }

                    is HomeScreenListResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
//                            successToast("LISTING ${apiRenderState.result.data?.list}")
                           "HOME DATA LISTING ${apiRenderState.result.data?.list}".logE()
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                    is UserProfileDetailResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "User Profile Fetched".logE()
                            prefs.userProfileDetail = apiRenderState.result
                            binding.userName.setText(prefs.userProfileDetail.userData?.detail?.uname.toString())
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