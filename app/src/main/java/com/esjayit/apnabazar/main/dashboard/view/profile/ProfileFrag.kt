package com.esjayit.apnabazar.main.dashboard.view.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Patterns
import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.CommonResponse
import com.esjayit.apnabazar.data.model.response.UserProfileDetailResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.profile.model.ProfileVM
import com.esjayit.apnabazar.main.entrymodule.view.SignInAct
import com.esjayit.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFrag : BaseFrag<FragmentProfileBinding, ProfileVM>(Layouts.fragment_profile) {

    override val vm: ProfileVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(requireActivity()) }

    override fun init() {
        if (prefs.userProfileDetail != null) {
            setUserProfileValue()
        } else {
            vm?.getUserProfile(userId = prefs.user.userId, installedId = prefs.installId!!)
        }
    }

    fun setUserProfileValue() {
        val detailModel = prefs.userProfileDetail.userData?.detail
        //Never Editable things
        binding.partyUserName.setText(detailModel?.username)
        binding.partyPhone1.setText(detailModel?.uphone1)
        //Editable things
        binding.partyName.setText(detailModel?.uname)
        binding.partyAddress.setText(detailModel?.uaddress)
        binding.partyPhone2.setText(detailModel?.uphone2)
        binding.partyEmail.setText(detailModel?.uemail)
        binding.partyCity.setText(detailModel?.ucity)
        binding.partyState.setText(detailModel?.ustate)
        binding.partyCountry.setText(detailModel?.ucountry)
        binding.partyGST.setText(detailModel?.gstno)
        binding.partyPanNo.setText(detailModel?.panno)
    }

    //Check for valid API or not
    fun vaildForAPI(): Boolean {
        var msg = ""
        if (binding.partyName.text.toString().isNullOrEmpty()) {
            msg = "Please enter valid user name"
        } else if (binding.partyAddress.text.toString().isNullOrEmpty()) {
            msg = "Please enter address"
        } else if (binding.partyPhone2.text.toString().isNullOrEmpty()) {
            msg = "Please enter Phone Number 2"
        } else if (!isValidMobile(binding.partyPhone2.text.toString())) {
            msg = "Please enter valid Phone Number 2"
        } else if (binding.partyEmail.text.toString().isNullOrEmpty()) {
            msg = "Please enter email address"
        } else if (!isValidMail(binding.partyEmail.text.toString())) {
            msg = "Please enter valid email address"
        } else if (binding.partyCity.text.toString().isNullOrEmpty()) {
            msg = "Please enter city"
        } else if (binding.partyState.text.toString().isNullOrEmpty()) {
            msg = "Please enter state"
        } else if (binding.partyCountry.text.toString().isNullOrEmpty()) {
            msg = "Please enter country"
        } else if (binding.partyGST.text.toString().isNullOrEmpty()) {
            msg = "Please enter gst no"
        } else if (binding.partyPanNo.text.toString().isNullOrEmpty()) {
            msg = "Please enter pancard no"
        }
        return if (msg.isEmpty()) {
            true
        } else {
            errorToast(msg)
            false
        }
    }

    private fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnEditProfile -> {
                "Edit Profile Button Tapped".logE()
                manageAPIs()
            }
            binding.btnLogout -> {
                manageAPIs(isLogout = true)
            }
        }
    }


    fun manageAPIs(isLogout: Boolean = false) {
        //For Logout API
        if (isLogout) {
            AlertDialog.Builder(requireActivity())
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        prefs.clearPrefs()
                        this.startActivity(Intent(requireActivity(), SignInAct::class.java))
                        requireActivity().finishAffinity()
                    })
                .setNegativeButton("No", null)
                .show()
        } else {
            if (vaildForAPI()) {
                progressDialog?.showProgress()
                vm.editUserProfile(
                    userId = prefs.user.userId,
                    name = binding.partyName.text.toString(),
                    address = binding.partyAddress.text.toString(),
                    phone1 = binding.partyPhone1.text.toString(),
                    phone2 = binding.partyPhone2.text.toString(),
                    email = binding.partyEmail.text.toString(),
                    city = binding.partyCity.text.toString(),
                    stateStr = binding.partyState.text.toString(),
                    country = binding.partyCountry.text.toString(),
                    gstNo = binding.partyGST.text.toString(),
                    panNo = binding.partyPanNo.text.toString(),
                    installedId = prefs.installId!!
                )
            } else {
                "In-VALID FOR API ERROR".logE()
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is CommonResponse -> {
                        progressDialog.hideProgress()
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            successToast(apiRenderState.result.message.toString())
                            vm.getUserProfile(
                                userId = prefs.user.userId,
                                installedId = prefs.installId!!
                            )
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                    is UserProfileDetailResponse -> {
                        progressDialog.hideProgress()
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
                progressDialog.hideProgress()
            }
            ApiRenderState.Loading -> {
                progressDialog.showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
                progressDialog.hideProgress()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
                progressDialog.hideProgress()
            }
        }
    }

}